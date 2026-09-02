package dev.abdullah.messseat.navigation

import kotlinx.serialization.Serializable

interface NavRoute

sealed interface AuthRoute : NavRoute {

    @Serializable
    data object Signup : AuthRoute

    @Serializable
    data object Login : AuthRoute

    @Serializable
    data class OtpVerify(val email: String,val isRecovery: Boolean=false) : AuthRoute

    @Serializable
    data object ForgetPwd : AuthRoute
}

sealed interface User : NavRoute{
    @Serializable
    data object Home : User

    @Serializable
    data object Chat : User

    @Serializable
    data object Settings : User
}

sealed interface Admin : NavRoute{

    @Serializable
    data object UserLists : Admin

    @Serializable
    data object Reports : Admin
}