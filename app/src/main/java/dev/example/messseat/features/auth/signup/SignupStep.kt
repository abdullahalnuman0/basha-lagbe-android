package dev.example.messseat.features.auth.signup

import dev.example.messseat.core.presentation.widgets.ProgressStep

enum class SignupStep(override val label: String) : ProgressStep {
    ROLE_SELECTION(""),
    BASIC_INFO("Basic Info"),
    ABOUT_YOU("About you"),
    LOCATION("Location"),
    SECURITY("Security");

    companion object {
        val progressSteps = listOf(
            BASIC_INFO,
            ABOUT_YOU,
            LOCATION,
            SECURITY
        )
    }
}