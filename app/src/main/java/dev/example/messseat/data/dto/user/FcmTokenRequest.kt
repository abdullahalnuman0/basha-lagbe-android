package dev.example.messseat.data.dto.user

import kotlinx.serialization.Serializable

@Serializable
data class FcmTokenRequest(val token: String)
