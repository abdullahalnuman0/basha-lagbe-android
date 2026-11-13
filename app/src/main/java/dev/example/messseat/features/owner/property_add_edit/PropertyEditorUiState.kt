package dev.example.messseat.features.owner.property_add_edit

import dev.example.messseat.data.dto.property.PropertyResponse
import dev.example.messseat.domain.enums.PropertyGender
import dev.example.messseat.domain.enums.PropertyType
import dev.example.messseat.domain.model.Room
import dev.example.messseat.features.auth.signup.LocationData

data class PropertyEditorUiState(
    val progressSteps: List<PropertyStep> = PropertyStep.progressSteps,
    val currentStep: PropertyStep = progressSteps[0],
    val lastStep: PropertyStep = progressSteps.last(),
    val isNavigatingForward: Boolean = true,

    // --- updated property ---
    val property: PropertyResponse?=null,

    // --- Step 1 - Basic Info ---
    val title: String = "",
    val titleError: String? = null,
    val description: String = "",
    val contactNumber: String = "",
    val contactNumberError: String? = null,

    // --- Step 2 - Location ---
    val division: String = LocationData.divisions[0],
    val divisionError: String? = null,
    val district: String = LocationData.districtByDivision[division]!![0],
    val districtError: String? = null,
    val area: String = "",
    val areaError: String? = null,
    val address: String = "",
    val addressError: String? = null,
    val institutionNearby: String = "",

    // --- Step 3 - Property Details ---
    val type: PropertyType? = null,
    val gender: PropertyGender? = null,
    val propertyDetailsError: String? = null,

    // --- Step 4 - Facilities ---
    val facilities: Set<String> = emptySet(),
    val facilitiesError: String? = null,

    // --- Step 5 - Rooms ---
    val rooms: List<Room> = emptyList(),
    val roomsError: String? = null,

    // --- loading ---
    val isLoading: Boolean = false,

    // --- dialog state --
    val showExitDialog : Boolean = false

)
