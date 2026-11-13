package dev.example.messseat.data.dto.tenant

import dev.example.messseat.data.enums.PropertyGender
import dev.example.messseat.data.enums.PropertyType
import dev.example.messseat.data.enums.RoomType
import kotlinx.serialization.Serializable

@Serializable
data class RoomTenantResponse(

    val ownerAvatarUrl: String?,
    val ownerId: Long,
    val ownerName: String,

    val address: String,
    val area: String,
    val availableSeats: Int,
    val contactNumber: String,
    val createdAt: String,
    val deposit: Int,
    val description: String,
    val district: String,
    val division: String,
    val facilities: List<String>,
    val gender: PropertyGender,
    val images: List<String>,
    val institutionNearby: String,

    val propertyId: Long,
    val propertyType: PropertyType,
    val rent: Int,
    val roomHeight: Int,
    val roomId: Long,
    val roomNumber: String,
    val roomType: RoomType,
    val roomWidth: Int,
    val title: String
)