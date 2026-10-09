package dev.abdullah.messseat.features.shared.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.abdullah.messseat.core.data.PreferencesManager
import dev.abdullah.messseat.core.data.SessionManager
import dev.abdullah.messseat.domain.enums.ThemeMode
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val preferences: PreferencesManager,
    private val session: SessionManager
) : ViewModel() {

    val themeMode = preferences.themeMode
        .stateIn(
            viewModelScope,
            SharingStarted.Eagerly,
            ThemeMode.SYSTEM
        )

    val user = session.userFlow
        .stateIn(
            viewModelScope,
            SharingStarted.Eagerly,
            null
        )

    private val notificationAllowed = MutableStateFlow(false)

    private val inAppNotificationEnable = preferences.inAppNotification
        .stateIn(viewModelScope, SharingStarted.Eagerly, true)

    private val soundEnable = preferences.inAppNotySound
        .stateIn(viewModelScope, SharingStarted.Eagerly, true)

    private val vibrationEnable = preferences.inAppNotyVibration
        .stateIn(viewModelScope, SharingStarted.Eagerly, true)

    val state = combine(
        themeMode,
        notificationAllowed,
        inAppNotificationEnable,
        soundEnable,
        vibrationEnable
    ) { theme, notyAllowed, inAppNoty, sound, vibration ->
        SettingPreferences(
            theme,
            notyAllowed,
            inAppNoty,
            sound,
            vibration
        )
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5_000),
        SettingPreferences()
    )

    fun refreshNotificationStatus(allowed: Boolean) {
        notificationAllowed.value = allowed
    }

    fun setThemeMode(mode: ThemeMode) = viewModelScope.launch {
        preferences.setThemeMode(mode)
    }

    fun setInAppNoty(value: Boolean) = viewModelScope.launch {
        preferences.setInAppNoty(value)
    }

    fun setInAppNotySound(value: Boolean) = viewModelScope.launch {
        preferences.setInAppNotySound(value)
    }

    fun setInAppNotyVibration(value: Boolean) = viewModelScope.launch {
        preferences.setInAppNotyVibration(value)
    }

    fun loggedOut(onSuccess: () -> Unit = {}) = viewModelScope.launch {
        session.clear()
        onSuccess()
    }
}