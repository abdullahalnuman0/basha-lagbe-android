package dev.example.messseat.data.mapper

import dev.example.messseat.data.enums.Role
import dev.example.messseat.domain.enums.UserRole

// -- Data -> Domain ---
fun Role.toDomain() = when (this) {
    Role.OWNER -> UserRole.OWNER
    Role.TENANT -> UserRole.TENANT
    else -> Role.TENANT
}

// --- Domain -> Data ---
fun UserRole.toData() = when(this){
    UserRole.TENANT -> Role.TENANT
    UserRole.OWNER -> Role.OWNER
}