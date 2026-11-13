package dev.example.messseat.features.auth

sealed interface AuthEvent {
    data class GoVerify(val email: String) : AuthEvent
    data class GoRecover(val email: String) : AuthEvent
    data class GoReset(val email: String) : AuthEvent
    data object GoLogin : AuthEvent
    data class Toast(val message: String, val isError: Boolean = false) : AuthEvent
}