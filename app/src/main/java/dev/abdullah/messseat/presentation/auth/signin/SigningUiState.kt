package dev.abdullah.messseat.presentation.auth.signin

data class SigningUiState(

    val email: String = "",
    val password: String = "",
    val error: String?=null,
    val isLoading: Boolean = false
)
