package dev.example.messseat.data.repository

import android.util.Log
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UnreadCountRepository @Inject constructor(
    private val repo: ChatRepository,
    private val notyRepo: NotificationRepository,
) {

    private val _unreadChatCount = MutableStateFlow(0L)
    val unreadChatCount = _unreadChatCount.asStateFlow()

    private val _unreadNotificationCount = MutableStateFlow(0L)
    val unreadNotificationCount = _unreadNotificationCount.asStateFlow()


    suspend fun updateChatCount() {
        _unreadChatCount.value = repo.unreadCount()
    }

    suspend fun updateNotificationCount() {
        _unreadNotificationCount.value = notyRepo.notificationUnread()
    }

    fun incrementUnreadNotyCount(v: Int = 1) {
        Log.d("Increment",_unreadNotificationCount.value.toString())
        _unreadNotificationCount.value += v
    }

    fun decrementUnreadNotyCount(v: Int = 1) {
        _unreadNotificationCount.value -= v
    }

}