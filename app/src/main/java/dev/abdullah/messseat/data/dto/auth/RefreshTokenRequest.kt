package dev.abdullah.messseat.data.dto.auth

import kotlinx.serialization.Serializable

@Serializable
data class RefreshTokenRequest(val refreshToken: String)
