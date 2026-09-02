package dev.abdullah.messseat.presentation.auth.signup

import dev.abdullah.messseat.domain.model.Gender
import dev.abdullah.messseat.domain.model.UserRole

data class SignupUiState(
    val currentStep: SignupStep = SignupStep.ROLE_SELECTION,
    val isNavigatingForward: Boolean = true,

    // --- Step 1 ---
    val selectedRole: UserRole? = null,

    // --- Step 2 - Basic Info ---
    val fullName: String = "",
    val fullNameError: String? = null,
    val email: String = "",
    val emailError: String? = null,
    val phone: String = "",
    val phoneError: String? = null,

    // --- Step 3 - About you ---
    val gender: Gender? = null,
    val genderError: String? = null,
    val institution: String = "",
    val isCustomInstitution: Boolean = false,
    val institutionError: String? = null,

    // --- Step 4 - Location ---
    val division: String = "",
    val divisionError: String? = null,
    val district: String = "",
    val districtError: String? = null,
    val area: String = "",
    val areaError: String? = null,

    // --- Step 5 - Security ---
    val password: String = "",
    val passwordError: String? = null,
    val confirmPassword: String = "",
    val confirmPasswordError: String? = null,
    val passwordRequirements: PasswordRequirements = PasswordRequirements(),
    val strength: PasswordStrength = PasswordStrength.EMPTY,
    val isAcceptTermsAndCondition: Boolean = false,
    val termsError: String?=null,

    // --- loading ---
    val isLoading: Boolean = false

)
