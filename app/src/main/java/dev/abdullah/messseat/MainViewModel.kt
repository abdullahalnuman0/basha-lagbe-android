package dev.abdullah.messseat

import android.annotation.SuppressLint
import android.content.Context
import android.media.AudioManager
import android.media.ToneGenerator
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import dev.abdullah.messseat.core.data.PreferencesManager
import dev.abdullah.messseat.core.data.SessionManager
import dev.abdullah.messseat.core.push.registerPushToken
import dev.abdullah.messseat.data.dto.notification.NotificationResponse
import dev.abdullah.messseat.data.repository.NotificationRepository
import dev.abdullah.messseat.data.repository.UnreadCountRepository
import dev.abdullah.messseat.data.repository.UserRepository
import dev.abdullah.messseat.domain.enums.ThemeMode
import dev.abdullah.messseat.features.shared.notification.NotificationHelper
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.time.Duration.Companion.seconds

@HiltViewModel
open class MainViewModel @Inject constructor(
    private val userRepo: UserRepository,
    private val unreadRepo: UnreadCountRepository,
    private val notyRepo: NotificationRepository,
    private val preferences: PreferencesManager,
    private val session: SessionManager,
    @ApplicationContext private val context: Context
) : ViewModel() {

    val themeMode = preferences.themeMode
        .stateIn(viewModelScope, SharingStarted.Eagerly, ThemeMode.SYSTEM)

    internal val notificationAllowed = MutableStateFlow(false)

    internal val inAppNotificationEnable = preferences.inAppNotification
        .stateIn(viewModelScope, SharingStarted.Eagerly, true)

    internal val soundEnable = preferences.inAppNotySound
        .stateIn(viewModelScope, SharingStarted.Eagerly, true)

    internal val vibrationEnable = preferences.inAppNotyVibration
        .stateIn(viewModelScope, SharingStarted.Eagerly, true)

    val isLoggedIn = session.isLoggedInFlow
        .stateIn(viewModelScope, SharingStarted.Eagerly, session.accessToken() != null)

    val user = session.userFlow
        .stateIn(viewModelScope, SharingStarted.Eagerly, null)

    fun loggedOut(onSuccess: () -> Unit = {}) = viewModelScope.launch {
        session.clear()
        onSuccess()
    }

    // ---- Banner state ----
    private val _banner = MutableStateFlow<NotificationResponse?>(null)
    val banner = _banner.asStateFlow()

    // ব্যানারে ক্লিক করলে NavHost এ নেভিগেট করার ইভেন্ট (একবারই কনজিউম হয়)
    private val _openNotification = Channel<NotificationResponse>(Channel.BUFFERED)
    val openNotification = _openNotification.receiveAsFlow()

    private var bannerSuppressed = false   // নোটিফিকেশন স্ক্রিনে থাকলে ব্যানার লাগবে না
    private var lastBannerId = -1L         // ডুপ্লিকেট ঠেকাতে
    private val processedNotificationIds = LinkedHashSet<Long>()

    val unreadChat = unreadRepo.unreadChatCount

    val unreadNotification = unreadRepo.unreadNotificationCount

    private var heartbeatJob: Job? = null
    var ready: Boolean = false
        private set

    init {
        viewModelScope.launch {
            ready = true
            unreadRepo.updateChatCount()
            unreadRepo.updateNotificationCount()
            startHeartbeat()
            observeLiveNotifications()
        }
    }

    fun onRegisterPush() = registerPushToken(userRepo, viewModelScope)


    fun refreshNotificationStatus(allowed: Boolean) {
        notificationAllowed.value = allowed
    }

    //========================
    // Live notification
    //========================
    @OptIn(ExperimentalCoroutinesApi::class)
    private fun observeLiveNotifications() {
        viewModelScope.launch {
            session.userFlow
                .map { it?.id }
                .distinctUntilChanged()
                .onEach { if (it == null) _banner.value = null }   // লগআউটে ব্যানার মুছে ফেলো
                .flatMapLatest { id ->
                    if (id == null) emptyFlow() else liveNotifications(id)
                }
                .collect { onIncoming(it) }
        }
    }

    // কানেকশন ছিঁড়ে গেলে ৩ সেকেন্ড পর আবার সাবস্ক্রাইব
    private fun liveNotifications(userId: Long): Flow<NotificationResponse> = flow {
        while (true) {
            emitAll(notyRepo.liveMessage(userId).catch { })
            delay(3.seconds)
        }
    }

    /*private fun onIncoming(noty: NotificationResponse) {
        if (noty.read || bannerSuppressed || noty.id == lastBannerId) return
        unreadRepo.incrementUnreadNotyCount()        // বেল আইকনের কাউন্ট আপডেট
        lastBannerId = noty.id

        if (inAppNotificationEnable.value) {
            playNotificationSound()
            _banner.value = noty
        }
      }*/
    private fun onIncoming(noty: NotificationResponse) {
        if (noty.read || bannerSuppressed) return

        // একই notification পুনরায় process হতে দেবে না
        if (!processedNotificationIds.add(noty.id)) return

        // Memory সীমিত রাখো
        if (processedNotificationIds.size > 500) {
            processedNotificationIds.remove(
                processedNotificationIds.first()
            )
        }

        lastBannerId = noty.id

        unreadRepo.incrementUnreadNotyCount()

        if (inAppNotificationEnable.value) {
            playNotificationSound()
            _banner.value = noty
        }
    }

    fun dismissBanner(id: Long) {
        // টাইমআউট দেরিতে এলে নতুন নোটিফিকেশন যেন মুছে না যায়
        _banner.update { if (it?.id == id) null else it }
    }

    fun onBannerClick(noty: NotificationResponse) {
        dismissBanner(noty.id)
        viewModelScope.launch {
            _openNotification.send(noty)
            notyRepo.markNotificationRead(noty.id)
            unreadRepo.decrementUnreadNotyCount()
        }
    }

    fun setBannerSuppressed(value: Boolean) {
        bannerSuppressed = value
    }


    //===========================
    // User heartbeat
    //===========================
    private fun startHeartbeat() {
        if (heartbeatJob?.isActive == true) return

        heartbeatJob = viewModelScope.launch {
            while (true) {
                userRepo.heartbeat()
                delay(100.seconds)
            }
        }
    }

    private fun stopHeartbeat() {
        heartbeatJob?.cancel()
        heartbeatJob = null
    }

    @SuppressLint("EmptySuperCall")
    override fun onCleared() {
        heartbeatJob?.cancel()
        super.onCleared()
    }


    private fun playNotificationSound() {
        NotificationHelper.inAppNotyAlert(
            context,
            soundEnable.value,
            vibrationEnable.value
        )
        /*runCatching {
            val toneGenerator = ToneGenerator(
                AudioManager.STREAM_NOTIFICATION,
                80
            )

            toneGenerator.startTone(
                ToneGenerator.TONE_PROP_BEEP,
                150
            )

            toneGenerator.release()
        }*/
    }
}