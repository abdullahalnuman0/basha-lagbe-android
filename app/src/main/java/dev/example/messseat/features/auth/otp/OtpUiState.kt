package dev.example.messseat.features.auth.otp

data class OtpUiState(
    val otp: String = "",
    val resendCooldown: Int = 30,
    val resendCount: Int = 0,
    val isResendCount: Boolean = false,
    val isSending: Boolean = false,
    val isLoading: Boolean = false
)
