package dev.abdullah.messseat.core.data

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
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
    }

    val themeMode: Flow<ThemeMode> = dataStore.data.map { prefs ->
        runCatching { ThemeMode.valueOf(prefs[Keys.THEME] ?: ThemeMode.LIGHT.name) }
            .getOrDefault(ThemeMode.LIGHT)
    }

    suspend fun setThemeMode(mode: ThemeMode) = dataStore.edit { prefs ->
        prefs[Keys.THEME] = mode.name
    }
}