package dev.abdullah.messseat.domain.model

import dev.abdullah.messseat.data.dto.user.UserResponse
import dev.abdullah.messseat.data.enums.Gender
import dev.abdullah.messseat.data.enums.Role
import dev.abdullah.messseat.data.enums.UserStatus
import java.time.Instant
import kotlin.toString

data class User(
    val id: Long = 0,
    val fullName: String = "",
    val email: String = "",
    val phone: String = "",
    val roles: List<Role> = emptyList(),
    val gender: Gender = Gender.MALE,
    val status: UserStatus = UserStatus.ACTIVE,
    val emailVerified: Boolean = false,
    val division: String? = null,
    val district: String? = null,
    val area: String? = null,
    val institution: String? = null,
    val avatarUrl: String? = null,
    val createdAt: Instant = Instant.now(),
    val suspensionEndAt: Instant? = null,
    val suspensionReason: String? = null,
    val deletionScheduledAt: Instant? = null
) {
    fun toData(): UserResponse {
        return UserResponse(
            id = id,
            fullName = fullName,
            email = email,
            phone = phone,
            roles = roles.map { it.name },
            gender = gender.name,
            status = status.name,
            emailVerified = emailVerified,
            division = division,
            district = district,
            area = area,
            institution = institution,
            avatarUrl = avatarUrl,
            createdAt = createdAt.toString(),
            suspensionEndAt = suspensionEndAt?.toString(),
            suspensionReason = suspensionReason,
            deletionScheduledAt = deletionScheduledAt?.toString()
        )
    }
}