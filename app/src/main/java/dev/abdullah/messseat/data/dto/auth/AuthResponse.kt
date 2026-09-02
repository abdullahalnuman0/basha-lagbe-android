package dev.abdullah.messseat.data.dto.auth

import dev.abdullah.messseat.data.dto.user.UserResponse


data class AuthResponse(
    val accessToken: String = "",
    val refreshToken: String = "",
    val tokenType: String? = null,
    val user: UserResponse = UserResponse()
)

