package dev.abdullah.messseat.presentation.auth.otp

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.abdullah.messseat.core.network.ApiResult
import dev.abdullah.messseat.data.dto.auth.MessageResponse
import dev.abdullah.messseat.data.repository.AuthRepository
import dev.abdullah.messseat.presentation.auth.AuthEvent
import dev.abdullah.messseat.presentation.auth.BaseAuthViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.time.Duration.Companion.seconds

@HiltViewModel
class OtpViewModel @Inject constructor(
    private val repo: AuthRepository
) : BaseAuthViewModel() {

    private val _uiState = MutableStateFlow(OtpUiState())
    val uiState = _uiState.asStateFlow()

    // --- best practice for updating state ---
    private inline fun updateState(
        crossinline update: OtpUiState.() -> OtpUiState
    ) {
        _uiState.update { it.update() }
    }

    private var resendJob: Job? = null

    override fun onCleared() {
        resendJob?.cancel()
        super.onCleared()
    }


    fun onOtpChange(value: String) =
        updateState { copy(otp = value) }


    fun resend(email: String, recover: Boolean) {

        if (_uiState.value.isLoading || _uiState.value.isResendCount) return

        startResendCount()

        updateState { copy(isLoading = true, isSending = true) }

        viewModelScope.launch {
            when (val r = repo.resendOtp(email)) {
                is ApiResult.Error -> emit(AuthEvent.Toast(r.message))
                is ApiResult.Success<*> -> emit(
                    AuthEvent.Toast(
                        (r.data as MessageResponse).message ?: "A new code has been sent."
                    )
                )
            }

            updateState { copy(isLoading = false, isSending = false) }
        }
    }

    private fun startResendCount() {
        resendJob?.cancel()

        resendJob = viewModelScope.launch {
            updateState {
                copy(
                    resendCooldown = 30 + (15 * this.resendCount),
                    isResendCount = true,
                    resendCount = this.resendCount + 1
                )
            }

            while (_uiState.value.resendCooldown > 0) {
                delay(1.seconds)
                updateState {
                    copy(resendCooldown = (this.resendCooldown - 1).coerceAtLeast(0))
                }
            }

            updateState { copy(isResendCount = false) }
        }
    }


    // --- verify the otp ---
    fun verifyOtp(email: String, recover: Boolean) {
        viewModelScope.launch {
            updateState { copy(isLoading = true) }

            when (val r = repo.verifyOtp(email, _uiState.value.otp)) {
                is ApiResult.Error -> emit(AuthEvent.Toast(r.message))
                is ApiResult.Success<*> -> emit(AuthEvent.Toast(r.data.toString()))
            }

            updateState { copy(isLoading = false) }
        }
    }


}