package dev.abdullah.messseat.core.data

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import dev.abdullah.messseat.domain.enums.ThemeMode
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map


class PreferencesManager(
    private val dataStore: DataStore<Preferences>
) {
    private object Keys {
        val THEME = stringPreferencesKey("theme_mode")
        val IN_APP_NOTY = booleanPreferencesKey("in_app_noty")
        val IN_APP_NOTY_SOUND = booleanPreferencesKey("in_app_noty_sound")
        val IN_APP_NOTY_VIBRATION = booleanPreferencesKey("in_app_noty_vibration")
    }

    val themeMode: Flow<ThemeMode> = dataStore.data.map { prefs ->
        runCatching { ThemeMode.valueOf(prefs[Keys.THEME] ?: ThemeMode.LIGHT.name) }
            .getOrDefault(ThemeMode.LIGHT)
    }

    val inAppNotification = dataStore.data.map { prefs ->
        runCatching { prefs[Keys.IN_APP_NOTY] ?: true }
            .getOrDefault(true)
    }

    val inAppNotySound = dataStore.data.map { prefs ->
        runCatching { prefs[Keys.IN_APP_NOTY_SOUND] ?: true }
            .getOrDefault(true)
    }

    val inAppNotyVibration = dataStore.data.map { prefs ->
        runCatching { prefs[Keys.IN_APP_NOTY_VIBRATION] ?: true }
            .getOrDefault(true)
    }

    //============= Set =================
    suspend fun setThemeMode(mode: ThemeMode) = dataStore.edit { prefs ->
        prefs[Keys.THEME] = mode.name
    }

    suspend fun setInAppNoty(v: Boolean) = dataStore.edit { prefs ->
        prefs[Keys.IN_APP_NOTY] = v
    }

    suspend fun setInAppNotySound(v: Boolean) = dataStore.edit { prefs ->
        prefs[Keys.IN_APP_NOTY_SOUND] = v
    }

    suspend fun setInAppNotyVibration(v: Boolean) = dataStore.edit { prefs ->
        prefs[Keys.IN_APP_NOTY_VIBRATION] = v
    }
}