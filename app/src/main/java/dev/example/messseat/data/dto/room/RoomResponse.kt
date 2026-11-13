package dev.example.messseat.data.dto.room

import dev.example.messseat.data.enums.RoomType
import dev.example.messseat.data.serializer.InstantSerializer
import dev.example.messseat.domain.model.Room
import kotlinx.serialization.Serializable
import java.time.Instant

@Serializable
data class RoomResponse(
    val totalViews: Long = 0,
    val availableSeats: Int,
    val deposit: Int,
    val id: Long,
    val images: List<String>,
    val rent: Int,
    val roomHeight: Int,
    val roomNumber: String,
    val roomWidth: Int,
    val type: RoomType,
    @Serializable(InstantSerializer::class) val createdAt: Instant
) {
    fun toRoom() = Room(
        totalViews,
        availableSeats,
        deposit,
        id,
        images,
        rent,
        roomHeight,
        roomNumber,
        roomWidth,
        type,
        createdAt
    )
}