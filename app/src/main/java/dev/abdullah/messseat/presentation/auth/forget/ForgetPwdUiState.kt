package dev.abdullah.messseat.presentation.auth.forget

import dev.abdullah.messseat.presentation.auth.signup.PasswordRequirements
import dev.abdullah.messseat.presentation.auth.signup.PasswordStrength
import dev.abdullah.messseat.presentation.auth.signup.SignupStep

data class ForgetPwdUiState(
    val currentStep: ForgetPwdStep = ForgetPwdStep.EMAIL,
    val isNavForward: Boolean = true,

    val email: String = "",
    val emailError: String? = null,
    val otpCode: String = "",
    val password: String = "",
    val passwordError: String? = null,
    val passwordRequirements: PasswordRequirements = PasswordRequirements(),
    val strength: PasswordStrength = PasswordStrength.EMPTY,

    val isLoading: Boolean = false
)