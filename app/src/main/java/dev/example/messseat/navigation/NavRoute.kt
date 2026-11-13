package dev.example.messseat.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ChatBubble
import androidx.compose.material.icons.outlined.Dashboard
import androidx.compose.material.icons.outlined.LocationCity
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.ui.graphics.vector.ImageVector
import dev.example.messseat.data.dto.property.PropertyResponse
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

sealed interface SharedRoute : NavRoute {
    @Serializable
    data object Settings : SharedRoute

    @Serializable
    data object User : SharedRoute

    @Serializable
    data object Chat : SharedRoute

    @Serializable
    data class Conversation(val id: Long) : SharedRoute

    @Serializable
    data object Notification : SharedRoute

}

sealed interface TenantRoute : NavRoute {
    @Serializable
    data object Home : TenantRoute

    @Serializable
    data object Search : TenantRoute

    // --- others routes ---
    @Serializable
    data class RoomDetails(val roomId: Long) : TenantRoute

    @Serializable
    data object SavedPost : TenantRoute

    companion object {
        val TABS = setOf(
            BottomNavItem("Home", Icons.Outlined.Dashboard, Home),
            BottomNavItem("Search", Icons.Outlined.Search, Search),
            BottomNavItem("Chat", Icons.Outlined.ChatBubble, SharedRoute.Chat, true),
            BottomNavItem("Settings", Icons.Outlined.Settings, SharedRoute.Settings),
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

    // --- others routes ---
    @Serializable
    data class Editor(val property: PropertyResponse? = null) : OwnerRoute

    @Serializable
    data class Details(val propertyId: Long) : OwnerRoute

    companion object {
        val TABS = setOf(
            BottomNavItem("Home", Icons.Outlined.Dashboard, Home),
            BottomNavItem("Listing", Icons.Outlined.LocationCity, Listings),
            BottomNavItem("Chat", Icons.Outlined.ChatBubble, SharedRoute.Chat, true),
            BottomNavItem("Settings", Icons.Outlined.Settings, SharedRoute.Settings),
        )

        fun showBottom(currentRoute: String?) = TABS.any { item ->
            item.routeName == currentRoute
        }

    }
}

data class BottomNavItem(
    val label: String,
    val icon: ImageVector,
    val route: NavRoute,
    val badged: Boolean = false
) {
    val routeName: String
        get() = route::class.qualifiedName.orEmpty()
}