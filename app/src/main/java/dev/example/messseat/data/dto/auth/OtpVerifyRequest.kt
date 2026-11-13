package dev.example.messseat.data.dto.auth

import kotlinx.serialization.Serializable

@Serializable
data class OtpVerifyRequest(
    val email: String,
    val code: String
)
