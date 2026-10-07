package dev.abdullah.messseat.navigation

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Logout
import androidx.compose.material.icons.outlined.Badge
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Phone
import androidx.compose.material.icons.outlined.School
import androidx.compose.material.icons.outlined.Verified
import androidx.compose.material.icons.outlined.Warning
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import dev.abdullah.messseat.data.dto.tenant.RoomTenantResponse
import dev.abdullah.messseat.data.enums.UserStatus
import dev.abdullah.messseat.domain.model.User
import dev.abdullah.messseat.features.common.settings.SettingsScreen
import dev.abdullah.messseat.features.common.user.ProfileCard
import dev.abdullah.messseat.features.common.user.UserScreen
import dev.abdullah.messseat.features.tenant.home.HomeScreen
import dev.abdullah.messseat.features.tenant.room_details.RoomDetailsScreen
import dev.abdullah.messseat.features.tenant.search.SearchScreen
import dev.abdullah.messseat.navigation.navtype.roomTenantResponseNavType
import kotlin.reflect.typeOf

@Composable
fun TenantNavHost(
    modifier: Modifier = Modifier,
    user: User?,
    loggedOut: () -> Unit
) {

    val navController = rememberNavController()
    val backStack by navController.currentBackStackEntryAsState()
    val currentRoute = backStack?.destination?.route

    fun navigateBottom(route: NavRoute) =
        navController.navigate(route) {
            popUpTo(navController.graph.findStartDestination().id) {
                saveState = true
            }
            launchSingleTop = true
            restoreState = true
        }

    fun navigate(route: NavRoute) =
        navController.navigate(route)

    Scaffold(
        bottomBar = {
            if (TenantRoute.showBottom(currentRoute))
                BottomNavigation(
                    navItems = TenantRoute.TABS,
                    currentRoute = currentRoute,
                    onNavigate = ::navigateBottom
                )
        }
    ) { innerPadding ->

        NavHost(
            navController = navController,
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding),
            startDestination = TenantRoute.Home,
            // --- animation ---
            enterTransition = enterPush(),
            exitTransition = exitPush(),
            popEnterTransition = enterPop(),
            popExitTransition = exitPop()
        ) {

            composable<TenantRoute.Home> {
                HomeScreen(
                    user = user,
                    onNavigateBottom = ::navigateBottom,
                    onNavigate = ::navigate,
                    loggedOut = loggedOut
                )
            }

            composable<TenantRoute.Search> {
                SearchScreen(onNavigate = ::navigate)
            }

            composable<TenantRoute.Chat> {
                Box(
                    modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colorScheme.background)
                        .padding(horizontal = 16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Coming soon...")
                }
            }

            composable<CommonRoute.Settings> {
                SettingsScreen(
                    onNavigate = ::navigate
                )
            }

            // --- others screens ---
            composable<TenantRoute.RoomDetails>(
                typeMap = mapOf(typeOf<RoomTenantResponse>() to roomTenantResponseNavType)
            ) {
                val toRoute = it.toRoute<TenantRoute.RoomDetails>()
                RoomDetailsScreen(
                    room = toRoute.room,
                    onBack = navController::popBackStack
                )
            }

            composable <CommonRoute.User>{
                UserScreen(
                    onBack = navController::popBackStack
                )
            }

        }
    }

}

