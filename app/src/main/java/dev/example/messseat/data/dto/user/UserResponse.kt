package dev.example.messseat.data.dto.user

import dev.example.messseat.domain.model.User
import dev.example.messseat.data.enums.Gender
import dev.example.messseat.data.enums.Role
import dev.example.messseat.data.enums.UserStatus
import kotlinx.serialization.Serializable
import java.time.Instant

@Serializable
class UserResponse(

    val id: Long = 0,
    val fullName: String = "",
    val email: String = "",
    val phone: String = "",
    val roles: List<String> = emptyList(),
    val gender: String = Gender.MALE.name,
    val status: String = UserStatus.ACTIVE.name,
    val emailVerified: Boolean = false,
    val division: String? = null,
    val district: String? = null,
    val area: String? = null,
    val institution: String? = null,
    val avatarUrl: String? = null,
    val createdAt: String? = null,
    val suspensionEndAt: String? = null,
    val suspensionReason: String? = null,
    val deletionScheduledAt: String? = null
) {
    fun toDomain(): User {
        return User(
            id = id,
            fullName = fullName,
            email = email,
            phone = phone,
            roles = roles.map { Role.valueOf(it) },
            gender = Gender.valueOf(gender),
            status = UserStatus.valueOf(status),
            emailVerified = emailVerified,
            division = division,
            district = district,
            area = area,
            institution = institution,
            avatarUrl = avatarUrl,
            createdAt = Instant.parse(createdAt),
            suspensionEndAt = suspensionEndAt?.let { Instant.parse(it) },
            suspensionReason = suspensionReason,
            deletionScheduledAt = deletionScheduledAt?.let { Instant.parse(it) }
        )

    }
}
