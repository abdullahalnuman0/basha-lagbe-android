package dev.example.messseat.features.auth.signin

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.example.messseat.core.network.ApiResult
import dev.example.messseat.data.repository.AuthRepository
import dev.example.messseat.features.auth.AuthEvent
import dev.example.messseat.features.auth.BaseAuthViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SigningViewModel @Inject constructor(
    private val repo: AuthRepository
) : BaseAuthViewModel() {

    private val _uiState = MutableStateFlow<SigningUiState>(SigningUiState())
    val uiState = _uiState.asStateFlow()

    fun onEmailChange(v: String) = _uiState.update { it.copy(email = v, error = null) }
    fun onPdwChange(v: String) = _uiState.update { it.copy(password = v, error = null) }

    fun login() {
        viewModelScope.launch {

            _uiState.update { it.copy(isLoading = true) }
            val state = _uiState.value

            when (val r = repo.login(state.email.trim(), state.password.trim())) {
                is ApiResult.Error -> {
                    _uiState.update { it.copy(error = r.message) }
                    when (r.code) {
                        "EMAIL_NOT_VERIFIED" -> emit(AuthEvent.GoVerify(state.email.trim()))
                        else -> {}
                    }
                }

                is ApiResult.Success<*> -> {} // session swap navigates automatically
            }

            _uiState.update { it.copy(isLoading = false) }

        }
    }
}