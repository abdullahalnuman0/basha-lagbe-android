package dev.abdullah.messseat.data.dto.property

import kotlinx.serialization.Serializable

@Serializable
data class OwnerDashboardSummaryResponse(
    val activeListings: Long,
    val pendingApproval: Long,
    val availableSeats: Long,
    val totalRoom: Long,

    val approvedRoom: Long,
    val pendingRoom: Long,
)