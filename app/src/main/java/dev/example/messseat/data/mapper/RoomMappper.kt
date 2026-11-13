package dev.example.messseat.data.mapper

import dev.example.messseat.data.dto.room.RoomRequest
import dev.example.messseat.domain.model.Room

fun RoomRequest.toDomain() = Room(
    availableSeats = this.availableSeats,
    deposit = this.deposit,
    images = this.images,
    rent = this.rent,
    roomHeight = this.roomHeight,
    roomNumber = this.roomNumber,
    roomWidth = this.roomWidth,
    type = this.type,
)