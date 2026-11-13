package dev.example.messseat.data.dto.room

import dev.example.messseat.data.enums.RoomType
import kotlinx.serialization.Serializable

@Serializable
data class RoomRequest(
    val availableSeats: Int,
    val deposit: Int=0,
    val images: List<String>,
    val rent: Int,
    val roomHeight: Int,
    val roomNumber: String,
    val roomWidth: Int,
    val type: RoomType
)