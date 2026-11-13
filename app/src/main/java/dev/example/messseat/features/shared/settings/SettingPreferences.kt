package dev.example.messseat.features.shared.settings

import dev.example.messseat.domain.enums.ThemeMode

data class SettingPreferences(
    val themeMode: ThemeMode = ThemeMode.SYSTEM,
    val notificationsAllowed: Boolean = true,
    val inAppNotificationEnable: Boolean = true,
    val soundEnabled: Boolean = true,
    val vibrationEnabled: Boolean = true,
)
