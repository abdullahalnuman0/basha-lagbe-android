package dev.abdullah.messseat.features.common.settings

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.abdullah.messseat.MainViewModel
import dev.abdullah.messseat.core.data.PreferencesManager
import dev.abdullah.messseat.core.data.SessionManager
import dev.abdullah.messseat.domain.enums.ThemeMode
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val preferences: PreferencesManager,
    private val session: SessionManager
) : MainViewModel(preferences, session) {

    // Settings-specific code এখানে

    fun setThemeMode(mode: ThemeMode) = viewModelScope.launch {
        preferences.setThemeMode(mode)
    }

}