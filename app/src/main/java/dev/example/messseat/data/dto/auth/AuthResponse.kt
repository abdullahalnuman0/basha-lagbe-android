package dev.example.messseat.data.dto.auth

import dev.example.messseat.data.dto.user.UserResponse
import kotlinx.serialization.Serializable


@Serializable
data class AuthResponse(
    val accessToken: String = "",
    val refreshToken: String = "",
    val tokenType: String? = null,
    val user: UserResponse = UserResponse()
)

