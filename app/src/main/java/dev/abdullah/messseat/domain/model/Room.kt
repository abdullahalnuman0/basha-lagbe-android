package dev.abdullah.messseat.domain.model

import dev.abdullah.messseat.data.dto.room.RoomResponse
import dev.abdullah.messseat.data.enums.RoomType
import java.time.Instant

data class Room(
    val totalViews: Long = 0,
    val availableSeats: Int = 0,
    val deposit: Int = 0,
    val id: Long = 0,
    val images: List<String> = emptyList(),
    val rent: Int = 0,
    val roomHeight: Int = 0,
    val roomNumber: String = "",
    val roomWidth: Int = 0,
    val type: RoomType? = null,
    val createdAt: Instant = Instant.now()
){
    fun toRoomResponse() = RoomResponse(
        totalViews,
        availableSeats,
        deposit,
        id,
        images,
        rent,
        roomHeight,
        roomNumber,
        roomWidth,
        type!!,
        createdAt
    )
}
