package dev.example.messseat.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import dev.example.messseat.data.dto.tenant.RoomTenantResponse
import dev.example.messseat.data.enums.NotyRefType
import dev.example.messseat.domain.model.User
import dev.example.messseat.features.shared.chat.ChatScreen
import dev.example.messseat.features.shared.conversation.ConversationScreen
import dev.example.messseat.features.shared.notification.InAppNotificationHost
import dev.example.messseat.features.shared.notification.NotificationScreen
import dev.example.messseat.features.shared.notification.target
import dev.example.messseat.features.shared.settings.SettingsScreen
import dev.example.messseat.features.shared.user.UserScreen
import dev.example.messseat.features.tenant.home.HomeScreen
import dev.example.messseat.features.tenant.room_details.RoomDetailsScreen
import dev.example.messseat.features.tenant.saved_post.SavedPostScreen
import dev.example.messseat.features.tenant.search.SearchScreen
import dev.example.messseat.mainViewModel
import dev.example.messseat.navigation.navtype.roomTenantResponseNavType
import dev.chrisbanes.haze.HazeState
import dev.chrisbanes.haze.hazeSource
import kotlin.reflect.typeOf

@Composable
fun TenantNavHost(
    modifier: Modifier = Modifier,
    user: User?,
    loggedOut: () -> Unit,
    onRequestNotifications: () -> Unit,
) {
    val viewModel = mainViewModel()
    val hazeState = remember { HazeState() }
    val banner by viewModel.banner.collectAsStateWithLifecycle()

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

    Box(Modifier.fillMaxSize()) {
        Box(
            Modifier
                .fillMaxSize()
                .hazeSource(hazeState)
        ) {
            Scaffold(
                bottomBar = {
                    if (TenantRoute.showBottom(currentRoute))
                        BottomNavigation(
                            navItems = TenantRoute.TABS,
                            currentRoute = currentRoute,
                            onNavigate = ::navigateBottom
                        )
                }
            )
            { innerPadding ->

                NavHost(
                    navController = navController,
                    modifier = modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                        .consumeWindowInsets(innerPadding),
                    startDestination = TenantRoute.Home,
                    // --- animation ---
                    enterTransition = enterPush(),
                    exitTransition = exitPush(),
                    popEnterTransition = enterPop(),
                    popExitTransition = exitPop()
                ) {

                    composable<TenantRoute.Home>(
                        enterTransition = tabEnter(), exitTransition = tabExit(),
                        popEnterTransition = tabEnter(), popExitTransition = tabExit()
                    ) {
                        HomeScreen(
                            user = user,
                            onNavigateBottom = ::navigateBottom,
                            onNavigate = ::navigate,
                            loggedOut = loggedOut
                        )
                    }

                    composable<TenantRoute.Search>(
                        enterTransition = tabEnter(), exitTransition = tabExit(),
                        popEnterTransition = tabEnter(), popExitTransition = tabExit()
                    ) {
                        SearchScreen(onNavigate = ::navigate)
                    }

                    composable<SharedRoute.Chat>(
                        enterTransition = tabEnter(), exitTransition = tabExit(),
                        popEnterTransition = tabEnter(), popExitTransition = tabExit()
                    ) {
                        ChatScreen(
                            user = user,
                            onNavigate = ::navigate,
                            loggedOut = loggedOut
                        )
                    }

                    composable<SharedRoute.Settings>(
                        enterTransition = tabEnter(), exitTransition = tabExit(),
                        popEnterTransition = tabEnter(), popExitTransition = tabExit()
                    ) {
                        SettingsScreen(
                            onNavigate = ::navigate,
                            onRequestNotifications = onRequestNotifications
                        )
                    }

                    // --- others screens ---
                    composable<TenantRoute.RoomDetails>(
                        typeMap = mapOf(typeOf<RoomTenantResponse>() to roomTenantResponseNavType)
                    ) {
                        RoomDetailsScreen(
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

                    composable<SharedRoute.Notification> {
                        NotificationScreen(nav = navController)
                    }

                    composable<TenantRoute.SavedPost> {
                        SavedPostScreen(
                            onNavigate = ::navigate,
                            onBack = navController::popBackStack
                        )
                    }

                }
            }
        }

        val isConversationScreen =
            backStack?.destination?.hasRoute<SharedRoute.Conversation>() == true

        val activeChatUserId = if (isConversationScreen) {
            backStack?.toRoute<SharedRoute.Conversation>()?.id
        } else {
            null
        }

        val shouldSuppressBanner = banner?.let { noty ->
            isConversationScreen &&
                    noty.refType == NotyRefType.CHAT &&
                    noty.refId == activeChatUserId
        } == true

        LaunchedEffect(banner?.id, activeChatUserId) {
            if (shouldSuppressBanner) {
                banner?.let { viewModel.dismissBanner(it.id) }
            }
        }

        InAppNotificationHost(
            banner = if (shouldSuppressBanner) null else banner,
            hazeState = hazeState,
            onClick = { noty ->
                viewModel.onBannerClick(noty)
                target(noty)?.let { navController.navigate(it) }
            },
            onDismiss = viewModel::dismissBanner,
            modifier = Modifier.align(Alignment.TopCenter),
        )

    }

}

