package dev.abdullah.messseat.features.tenant.saved_post

import dev.abdullah.messseat.data.dto.tenant.RoomTenantResponse

data class SavePostUiState(
    val rooms: List<RoomTenantResponse> = emptyList(),

    val error: String? = null,

    val isLoading: Boolean = true
)
