package dev.example.messseat.data.dto.auth

import dev.example.messseat.data.enums.Gender
import dev.example.messseat.data.enums.Role
import kotlinx.serialization.Serializable

@Serializable
data class RegisterRequest(
    val fullName: String,
    val email: String,
    val phone: String,
    val password: String,
    val gender: Gender,
    val role: Role,
    val division: String? = null,
    val district: String? = null,
    val area: String? = null,
    val institution: String? = null,
    val termsAccepted: Boolean = false
)


