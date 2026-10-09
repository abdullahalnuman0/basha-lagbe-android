package dev.abdullah.messseat.features.tenant.room_details

import dev.abdullah.messseat.data.dto.room.RoomResponse
import dev.abdullah.messseat.data.dto.tenant.RoomTenantResponse

data class RoomDetailsUiState(
    val roomId: Long = 0L,
    val room: RoomTenantResponse? = null,
    val isFavorite: Boolean = false,
    val isChatOpening: Boolean = false,

    val error: String? = null,

    val isLoading: Boolean = true,
)
