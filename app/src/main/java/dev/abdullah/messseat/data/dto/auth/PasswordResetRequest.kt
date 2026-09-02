package dev.abdullah.messseat.data.dto.auth

data class PasswordResetRequest(
    val email: String,
    val code: String,
    val newPassword: String
)
