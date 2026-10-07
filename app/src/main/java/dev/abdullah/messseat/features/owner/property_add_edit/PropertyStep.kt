package dev.abdullah.messseat.features.owner.property_add_edit

import dev.abdullah.messseat.core.presentation.widgets.ProgressStep

enum class PropertyStep(override val label: String) : ProgressStep {
    BASIC_INFO("Basic Info"),
    LOCATION("Location"),
    PROPERTY_DETAILS("Property Details"),
    FACILITIES("Facilities"),
    ROOMS("Rooms");

    companion object {
        val progressSteps = listOf(
            PROPERTY_DETAILS,
            BASIC_INFO,
            LOCATION,
            FACILITIES,
            ROOMS
        )

        val updateProgressSteps = listOf(
            PROPERTY_DETAILS,
            BASIC_INFO,
            LOCATION,
            FACILITIES,
        )

    }
}