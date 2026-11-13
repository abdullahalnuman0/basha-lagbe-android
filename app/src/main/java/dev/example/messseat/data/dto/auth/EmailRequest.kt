package dev.example.messseat.data.dto.auth

import kotlinx.serialization.Serializable

@Serializable
data class EmailRequest(val email: String)
