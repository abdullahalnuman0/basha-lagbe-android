package dev.abdullah.messseat.features.auth.forget

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.abdullah.messseat.core.network.ApiResult
import dev.abdullah.messseat.data.repository.AuthRepository
import dev.abdullah.messseat.features.auth.AuthEvent
import dev.abdullah.messseat.features.auth.BaseAuthViewModel
import dev.abdullah.messseat.features.auth.signup.PasswordRequirements
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ForgetPwdViewModel @Inject constructor(
    private val repo: AuthRepository
) : BaseAuthViewModel() {

    private val _uiState = MutableStateFlow(ForgetPwdUiState())
    val uiState = _uiState.asStateFlow()

    // --- Step 1 ----
    fun onEmailChange(value: String) =
        _uiState.update { it.copy(email = value.trim(), emailError = null) }

    private suspend fun validateEmail(): Boolean {

        val state = _uiState.value
        var emailError: String? = null
        val emailRegex = Regex("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")

        if (!emailRegex.matches(state.email.trim()))
            emailError = "Enter a valid email address."

        // check email already exists or not
        if (emailError == null) {
            when (val result = repo.forgotPassword(state.email.trim())) {
                is ApiResult.Error -> emailError = result.message
                else -> {}
            }
        }
        _uiState.update { it.copy(emailError = emailError) }


        return emailError == null
    }

    // --- Step 2 ----
    fun changeEmail() {
        _uiState.update { it.copy(email = "") }
        onGoBack()
    }

    fun onOtpChange(value: String) = _uiState.update { it.copy(otpCode = value) }

    fun onPasswordChange(value: String) = _uiState.update { state ->

        val pwd = value.trim()

        val requirements = PasswordRequirements(
            hasMinLength = pwd.length >= 8,
            hasUppercase = pwd.any(Char::isUpperCase),
            hasNumber = pwd.any(Char::isDigit),
            hasSpacialChar = pwd.any { !it.isLetterOrDigit() }
        )

        state.copy(
            password = pwd,
            passwordError = null,
            passwordRequirements = requirements,
            strength = calculatePasswordStrength(
                requirements = requirements,
                password = pwd
            )
        )
    }

    fun validatePassword(): Boolean {
        val state = _uiState.value
        if (!state.passwordRequirements.isValid) {
            _uiState.update { it.copy(passwordError = passwordMessage(state.passwordRequirements)) }
            return false
        }
        return true
    }

    // --- navigation ---
    fun onGoNext() {
        viewModelScope.launch {

            println("Calling")

            _uiState.update { it.copy(isLoading = true) }

            val current = _uiState.value.currentStep

            val validate = when (current) {
                ForgetPwdStep.EMAIL -> validateEmail()
                ForgetPwdStep.PASSWORD -> validatePassword()
            }

            if (!validate) {
                _uiState.update { it.copy(isLoading = false) }
                return@launch
            }


            val next = when (current) {
                ForgetPwdStep.EMAIL -> ForgetPwdStep.PASSWORD
                ForgetPwdStep.PASSWORD -> current
            }

            if (current == ForgetPwdStep.PASSWORD) {

                // --- forget password ---
                val state = _uiState.value
                when (val r = repo.resetPassword(state.email, state.otpCode, state.password)) {
                    is ApiResult.Error -> emit(AuthEvent.Toast(r.message))
                    is ApiResult.Success<*> -> {
                        emit(AuthEvent.Toast("Password changed — please log in"))
                        emit(AuthEvent.GoLogin)
                    }
                }

                _uiState.update { it.copy(isLoading = false) }

            } else {
                _uiState.update {
                    it.copy(
                        currentStep = next,
                        isNavForward = true,
                        isLoading = false
                    )
                }
            }

        }

    }

    fun onGoBack() {
        val previous = when (_uiState.value.currentStep) {
            ForgetPwdStep.EMAIL -> _uiState.value.currentStep
            ForgetPwdStep.PASSWORD -> ForgetPwdStep.EMAIL
        }

        _uiState.update {
            it.copy(
                currentStep = previous,
                isNavForward = false
            )
        }
    }

}