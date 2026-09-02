package dev.abdullah.messseat.presentation.auth.signup

enum class SignupStep(val number: Int, val label: String) {

    ROLE_SELECTION(1, ""),
    BASIC_INFO(2, "Basic Info"),
    ABOUT_YOU(3, "About you"),
    LOCATION(4, "Location"),
    SECURITY(5, "Security");

    companion object {


        val TOTAL_STEPS = SignupStep.entries.size
        val FROM_STEPS_COUNT = TOTAL_STEPS - 1

        val ACTIVE_INDEX: (Int) -> Int = {
            it - 2
        }
    }
}