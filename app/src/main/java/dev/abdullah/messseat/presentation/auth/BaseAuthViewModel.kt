package dev.abdullah.messseat.presentation.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.abdullah.messseat.presentation.auth.signup.PasswordRequirements
import dev.abdullah.messseat.presentation.auth.signup.PasswordStrength
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

abstract class BaseAuthViewModel : ViewModel() {

    private val _events = Channel<AuthEvent>(Channel.BUFFERED)
    val events = _events.receiveAsFlow()

    protected fun emit(event: AuthEvent) = viewModelScope.launch {
        _events.send(event)
    }

    protected fun calculatePasswordStrength(
        requirements: PasswordRequirements,
        password: String
    ): PasswordStrength {

        if (password.isEmpty()) {
            return PasswordStrength.EMPTY
        }

        val score = listOf(
            requirements.hasMinLength,
            requirements.hasUppercase,
            requirements.hasNumber,
            requirements.hasSpacialChar
        ).count { it }

        return when (score) {
            0, 1 -> PasswordStrength.WEAK
            2 -> PasswordStrength.FAIR
            3 -> PasswordStrength.MEDIUM
            4 -> PasswordStrength.STRONG
            else -> PasswordStrength.EMPTY
        }
    }

    protected fun passwordMessage(rqm: PasswordRequirements): String {
        val missing = buildList {
            if (!rqm.hasMinLength) add("at least 8 characters")
            if (!rqm.hasUppercase) add("1 uppercase letter")
            if (!rqm.hasNumber) add("1 number")
            if (!rqm.hasSpacialChar) add("1 special character")
        }

        return if (missing.isEmpty()) ""
        else "Password must include ${missing.joinToString(", ")}."

    }

}