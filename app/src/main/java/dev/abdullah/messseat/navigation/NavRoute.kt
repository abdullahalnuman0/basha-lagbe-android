package dev.abdullah.messseat.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Dashboard
import androidx.compose.material.icons.outlined.LocationCity
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.ui.graphics.vector.ImageVector
import dev.abdullah.messseat.data.dto.property.PropertyResponse
import kotlinx.serialization.Serializable

interface NavRoute

sealed interface AuthRoute : NavRoute {

    @Serializable
    data object Signup : AuthRoute

    @Serializable
    data object Login : AuthRoute

    @Serializable
    data class OtpVerify(val email: String, val isRecovery: Boolean = false) : AuthRoute

    @Serializable
    data object ForgetPwd : AuthRoute
}

sealed interface UserRoute : NavRoute {
    @Serializable
    data object Home : UserRoute

    @Serializable
    data object Chat : UserRoute

    @Serializable
    data object Settings : UserRoute
}

sealed interface OwnerRoute : NavRoute {

    // --- bottoms route ---
    @Serializable
    data object Home : OwnerRoute

    @Serializable
    data object Listings : OwnerRoute

    @Serializable
    data object Notifications : OwnerRoute

    // --- others routes ---
    @Serializable
    data class Editor(val property: PropertyResponse? = null) : OwnerRoute

    @Serializable
    data class Details(val propertyId: Long) : OwnerRoute

    companion object {
        val TABS = setOf(
            BottomNavItem("Home", Icons.Outlined.Dashboard, Home),
            BottomNavItem("Listing", Icons.Outlined.LocationCity, Listings),
            BottomNavItem("Notifications", Icons.Outlined.Notifications, Notifications),
        )

        fun showBottom(currentRoute: String?) = TABS.any { item ->
            item.routeName == currentRoute
        }

    }
}

data class BottomNavItem(
    val label: String,
    val icon: ImageVector,
    val route: NavRoute
) {
    val routeName: String
        get() = route::class.qualifiedName.orEmpty()
}