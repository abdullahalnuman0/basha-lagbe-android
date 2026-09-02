package dev.abdullah.messseat.presentation.auth.signup

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.abdullah.messseat.core.network.ApiResult
import dev.abdullah.messseat.data.dto.auth.EmailRequest
import dev.abdullah.messseat.data.dto.auth.RegisterRequest
import dev.abdullah.messseat.data.mapper.toData
import dev.abdullah.messseat.data.repository.AuthRepository
import dev.abdullah.messseat.domain.model.Gender
import dev.abdullah.messseat.domain.model.UserRole
import dev.abdullah.messseat.presentation.auth.AuthEvent
import dev.abdullah.messseat.presentation.auth.BaseAuthViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignupViewModel @Inject constructor(
    private val repo: AuthRepository
) : BaseAuthViewModel() {

    private val _uiState = MutableStateFlow(
        SignupUiState(
//            currentStep = SignupStep.SECURITY,
//            selectedRole = UserRole.OWNER,
//            fullName = "Abdullah Al Numan",
//            email = "abdullahalnumanb${(1..10000).random()}@gmail.com",
//            phone = "01754155296",
//            gender = Gender.MALE,
//            division = "Sylhet",
//            district = "Sylhet",
//            area = "Amberkhana",
//            password = "Ab!dullah1",
//            confirmPassword = "Ab!dullah1",
//            isAcceptTermsAndCondition = true
        )
    )
    val uiState = _uiState.asStateFlow()


    // --- Step 1 - Role ---
    fun onRoleSelected(role: UserRole) = _uiState.update { it.copy(selectedRole = role) }
    private fun canContinueFromRole(): Boolean = _uiState.value.selectedRole != null

    // --- Step 2 - Basic Info ---
    fun onFullNameChange(value: String) =
        _uiState.update { it.copy(fullName = value, fullNameError = null) }

    fun onEmailChange(value: String) = _uiState.update { it.copy(email = value, emailError = null) }
    fun onPhoneChange(value: String) = _uiState.update {
        it.copy(
            phone = value.filter { n -> n.isDigit() }.take(11),
            phoneError = null
        )
    }

    private suspend fun validateBasicInfo(): Boolean {

        val state = _uiState.value

        var nameError: String? = null
        var emailError: String? = null
        var phoneError: String? = null
        val phoneRegex = Regex("^01[3-9]\\d{8}$")
        val emailRegex = Regex("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")


        if (state.fullName.trim().isEmpty())
            nameError = "Please enter your full name."

        if (!emailRegex.matches(state.email.trim()))
            emailError = "Enter a valid email address."

        // check email already exists or not
        if (emailError == null) {
            when (val result = repo.verifyEmail(EmailRequest(state.email.trim()))) {
                is ApiResult.Error -> emailError = result.message
                else -> {}
            }
        }

        if (!phoneRegex.matches(state.phone))
            phoneError = "Enter a valid phone number."


        _uiState.update {
            it.copy(
                fullNameError = nameError,
                emailError = emailError,
                phoneError = phoneError
            )
        }



        return nameError == null && emailError == null && phoneError == null
    }


    // --- Step 3 - About you ---
    fun onGenderSelected(gender: Gender) =
        _uiState.update { it.copy(gender = gender, genderError = null) }

    fun onInstitutionChange(value: String) =
        _uiState.update { it.copy(institution = value, institutionError = null) }

    fun onInstitutionNotListedToggle(value: Boolean) =
        _uiState.update { it.copy(isCustomInstitution = value) }

    private fun validateAboutYou(): Boolean {
        var genderError: String? = null

        if (_uiState.value.gender == null)
            genderError = "Please select your gender"

        _uiState.update { it.copy(genderError = genderError) }

        return genderError == null
    }


    // --- step 4 - Location ---
    fun onDivisionSelected(value: String) =
        _uiState.update { it.copy(division = value, divisionError = null) }

    fun onDistrictSelected(value: String) =
        _uiState.update { it.copy(district = value, districtError = null) }

    fun onAreaSelected(value: String) =
        _uiState.update { it.copy(area = value, areaError = null) }

    private fun validateLocation(): Boolean {
        var divError: String? = null
        var disError: String? = null
        var areaError: String? = null

        val state = _uiState.value

        if (state.division.isEmpty())
            divError = "Please select your division."
        if (state.district.isEmpty())
            disError = "Please select your district."
        if (state.area.isEmpty())
            areaError = "Please enter your area."

        _uiState.update {
            it.copy(
                divisionError = divError,
                districtError = disError,
                areaError = areaError
            )
        }

        return divError == null && disError == null && areaError == null
    }

    // --- Step 5 - Security ---
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


    fun onConfirmPassChange(value: String) =
        _uiState.update { it.copy(confirmPassword = value.trim(), confirmPasswordError = null) }

    fun onTermsToggle(value: Boolean) =
        _uiState.update { it.copy(isAcceptTermsAndCondition = value, termsError = null) }

    private fun validateSecurity(): Boolean {
        val state = _uiState.value
        if (!state.passwordRequirements.isValid) {
            _uiState.update { it.copy(passwordError = passwordMessage(state.passwordRequirements)) }
            return false
        }

        var confirmPassError: String? = null
        var termsError: String? = null

        if (state.confirmPassword.isEmpty())
            confirmPassError = "Please re-enter your password."
        else if (state.password != state.confirmPassword)
            confirmPassError = "Passwords don't match"

        if (!state.isAcceptTermsAndCondition)
            termsError = "Please agree to the Terms & Conditions"

        _uiState.update {
            it.copy(
                confirmPasswordError = confirmPassError,
                termsError = termsError
            )
        }

        return confirmPassError == null && termsError == null
    }




    // --- Navigation ---
    fun onGoNext() {

        viewModelScope.launch {

            _uiState.update { it.copy(isLoading = true) }

            val current = _uiState.value.currentStep

            val validate = when (current) {
                SignupStep.ROLE_SELECTION -> canContinueFromRole()
                SignupStep.BASIC_INFO -> validateBasicInfo()
                SignupStep.ABOUT_YOU -> validateAboutYou()
                SignupStep.LOCATION -> validateLocation()
                SignupStep.SECURITY -> validateSecurity()
            }

            if (!validate) {
                _uiState.update { it.copy(isLoading = false) }
                return@launch
            }


            val next = when (current) {
                SignupStep.ROLE_SELECTION -> SignupStep.BASIC_INFO
                SignupStep.BASIC_INFO -> SignupStep.ABOUT_YOU
                SignupStep.ABOUT_YOU -> SignupStep.LOCATION
                SignupStep.LOCATION -> SignupStep.SECURITY
                SignupStep.SECURITY -> current
            }

            if (current == SignupStep.SECURITY) {

                // --- create user account ---
                val state = _uiState.value

                val req = RegisterRequest(
                    role = state.selectedRole!!.toData(),
                    fullName = state.fullName.trim(),
                    email = state.email.trim(),
                    phone = state.phone,
                    gender = state.gender!!.toData(), // Edit this,
                    institution = state.institution,
                    division = state.division,
                    district = state.district,
                    area = state.area,
                    password = state.password.trim(),
                    termsAccepted = state.isAcceptTermsAndCondition
                )

                when (val r = repo.register(req)) {
                    is ApiResult.Error -> {
                        emit(AuthEvent.Toast(r.message, isError = true))
                    }

                    is ApiResult.Success<*> -> {
                        emit(AuthEvent.GoVerify(state.email.trim()))
                    }

                }

                _uiState.update { it.copy(isLoading = false) }

            } else {
                _uiState.update {
                    it.copy(
                        currentStep = next,
                        isNavigatingForward = true,
                        isLoading = false
                    )
                }
            }

        }

    }

    fun onGoBack() {
        val previous = when (_uiState.value.currentStep) {
            SignupStep.BASIC_INFO -> SignupStep.ROLE_SELECTION
            SignupStep.ABOUT_YOU -> SignupStep.BASIC_INFO
            SignupStep.LOCATION -> SignupStep.ABOUT_YOU
            SignupStep.SECURITY -> SignupStep.LOCATION
            else -> _uiState.value.currentStep
        }

        _uiState.update {
            it.copy(
                currentStep = previous,
                isNavigatingForward = false
            )
        }
    }

}