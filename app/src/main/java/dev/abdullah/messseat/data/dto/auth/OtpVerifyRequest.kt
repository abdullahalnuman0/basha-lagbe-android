package dev.abdullah.messseat.data.dto.auth

data class OtpVerifyRequest(
    val email: String,
    val code: String
)
