package dev.abdullah.messseat.data.dto.property

import dev.abdullah.messseat.data.dto.room.RoomRequest
import dev.abdullah.messseat.data.enums.PropertyGender
import dev.abdullah.messseat.data.enums.PropertyType
import kotlinx.serialization.Serializable

@Serializable
data class PropertyRequest(
    val address: String,
    val area: String,
    val contactNumber: String,
    val description: String,
    val district: String,
    val division: String,
    val facilities: List<String>,
    val gender: PropertyGender,
    val institutionNearby: String?=null,
    val rooms: List<RoomRequest>,
    val title: String,
    val type: PropertyType
)