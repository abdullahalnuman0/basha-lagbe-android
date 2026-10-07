package dev.abdullah.messseat.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import dev.abdullah.messseat.data.dto.tenant.RoomTenantResponse
import dev.abdullah.messseat.domain.model.User
import dev.abdullah.messseat.features.shared.chat.ChatScreen
import dev.abdullah.messseat.features.shared.conversation.ConversationScreen
import dev.abdullah.messseat.features.shared.settings.SettingsScreen
import dev.abdullah.messseat.features.shared.user.UserScreen
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
            startDestination = SharedRoute.Chat,
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

            composable<SharedRoute.Chat> {
                ChatScreen(
                    user = user,
                    onNavigate = ::navigate,
                    loggedOut = loggedOut
                )
            }

            composable<SharedRoute.Settings> {
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
                    onNavigate = ::navigate,
                    onBack = navController::popBackStack
                )
            }

            composable<SharedRoute.User> {
                UserScreen(
                    onBack = navController::popBackStack
                )
            }

            composable<SharedRoute.Conversation> {
                ConversationScreen(onBack = navController::popBackStack)
            }

        }
    }

}

