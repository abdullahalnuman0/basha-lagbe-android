package dev.example.messseat.features.owner.property_details

import dev.example.messseat.data.dto.property.PropertyResponse

data class PropertyDetailsUiState(

    val propertyId: Long? = null,
    val property: PropertyResponse? = null,

    val searchText: String = "",

    val showDeleteAlert: Boolean = false,

    val isLoading: Boolean = true,
    val isRefreshing: Boolean = false,
    val isForceLoading: Boolean = false,

    val error: String? = null,

    )
