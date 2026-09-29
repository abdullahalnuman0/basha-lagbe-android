package dev.abdullah.messseat.data.dto.property

import dev.abdullah.messseat.data.dto.room.RoomResponse
import dev.abdullah.messseat.data.enums.PropertyGender
import dev.abdullah.messseat.data.enums.PropertyType
import dev.abdullah.messseat.data.serializer.InstantSerializer
import kotlinx.serialization.Serializable
import java.time.Instant

@Serializable
data class PropertyResponse(
    val totalViews: Long=0,
    val id: Long,
    val title: String,
    val description: String,
    val approved: Boolean,
    val division: String,
    val district: String,
    val area: String,
    val address: String,
    val contactNumber: String,
    val gender: PropertyGender,
    val type: PropertyType,
    val institutionNearby: String,
    val facilities: List<String>,
    val rooms: List<RoomResponse>,
    @Serializable(InstantSerializer::class) val createdAt: Instant
)
