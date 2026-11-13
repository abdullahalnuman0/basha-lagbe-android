package dev.example.messseat.data.dto.auth

import kotlinx.serialization.Serializable

@Serializable
data class PasswordResetRequest(
    val email: String,
    val code: String,
    val newPassword: String
)
