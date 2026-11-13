package dev.example.messseat.data.dto.tenant

import kotlinx.serialization.Serializable

@Serializable
data class PropertyTenantResponse(
    val ownerAvatarUrl: String?,
    val ownerId: Long,
    val ownerName: String,

    val address: String,
    val area: String,
    val contactNumber: String,
    val createdAt: String,
    val description: String,
    val district: String,
    val division: String,
    val facilities: List<String>,
    val gender: String,
    val institutionNearby: String,

    val propertyId: Long,
    val propertyType: String,
    val rooms: List<RoomTenantResponse>,
    val title: String,
    val totalAvailableSeats: Int,
    val totalRooms: Int
)