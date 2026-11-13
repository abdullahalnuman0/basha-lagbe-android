package dev.example.messseat.data.dto.room

import kotlinx.serialization.Serializable

@Serializable
data class RoomAvailabilityRequest(
    val roomId: Long,
    val availableSeats: Int
)
