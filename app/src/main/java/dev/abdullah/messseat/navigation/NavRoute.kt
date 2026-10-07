package dev.abdullah.messseat.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ChatBubble
import androidx.compose.material.icons.outlined.Dashboard
import androidx.compose.material.icons.outlined.LocationCity
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.ui.graphics.vector.ImageVector
import dev.abdullah.messseat.data.dto.property.PropertyResponse
import dev.abdullah.messseat.data.dto.tenant.RoomTenantResponse
import dev.abdullah.messseat.navigation.OwnerRoute.Listings
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

sealed interface CommonRoute : NavRoute {
    @Serializable
    data object Settings : CommonRoute

    @Serializable
    data object User : CommonRoute
}

sealed interface TenantRoute : NavRoute {
    @Serializable
    data object Home : TenantRoute

    @Serializable
    data object Search : TenantRoute

    @Serializable
    data object Chat : TenantRoute


    // --- others routes ---
    @Serializable
    data class RoomDetails(val room: RoomTenantResponse) : TenantRoute


    companion object {
        val TABS = setOf(
            BottomNavItem("Home", Icons.Outlined.Dashboard, Home),
            BottomNavItem("Search", Icons.Outlined.Search, Search),
            BottomNavItem("Chat", Icons.Outlined.ChatBubble, Chat),
            BottomNavItem("Settings", Icons.Outlined.Settings, CommonRoute.Settings),
        )

        fun showBottom(currentRoute: String?) = TABS.any { item ->
            item.routeName == currentRoute
        }

    }
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
            BottomNavItem("Settings", Icons.Outlined.Settings, CommonRoute.Settings),
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