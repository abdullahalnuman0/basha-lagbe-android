package dev.abdullah.messseat.features.owner.home

import dev.abdullah.messseat.data.dto.property.OwnerDashboardSummaryResponse
import dev.abdullah.messseat.data.dto.property.PropertyResponse

data class HomeUiState(

    val properties: List<PropertyResponse>? = null,
    val dashboardSummary: OwnerDashboardSummaryResponse? = null,

    val sortPosition: Int = 0,

    val isLoading: Boolean = false,       // initial loading
    val isRefreshing: Boolean = false,    // pull-to-refresh
    val error: String? = null,            // initial load error
    val refreshError: String? = null       // refresh error
)
