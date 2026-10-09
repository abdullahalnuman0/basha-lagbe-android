package dev.abdullah.messseat.data.dto.tenant

import kotlinx.serialization.Serializable

@Serializable
data class SavedRoomsRequest(
    val roomIds: List<Long>
)
