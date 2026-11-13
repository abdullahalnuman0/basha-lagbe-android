package dev.example.messseat.features.tenant.room_details

import dev.example.messseat.data.dto.tenant.RoomTenantResponse

data class RoomDetailsUiState(
    val roomId: Long = 0L,
    val room: RoomTenantResponse? = null,
    val isFavorite: Boolean = false,
    val isChatOpening: Boolean = false,

    val error: String? = null,

    val isLoading: Boolean = true,
)
