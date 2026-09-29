package dev.abdullah.messseat

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.abdullah.messseat.core.data.PreferencesManager
import dev.abdullah.messseat.core.data.SessionManager
import dev.abdullah.messseat.domain.enums.ThemeMode
import dev.abdullah.messseat.domain.model.User
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val preferences: PreferencesManager,
    private val session: SessionManager
) : ViewModel() {

    val themeMode = preferences.themeMode
        .stateIn(viewModelScope, SharingStarted.Eagerly, ThemeMode.SYSTEM)
    val isLoggedIn = session.isLoggedInFlow
        .stateIn(viewModelScope, SharingStarted.Eagerly, session.accessToken() != null)

    val user = session.userFlow
        .stateIn(viewModelScope, SharingStarted.Eagerly, null)

    fun loggedOut() = viewModelScope.launch {
        session.clear()
    }

    var ready: Boolean = false
        private set

    init {
        ready = true
    }
}