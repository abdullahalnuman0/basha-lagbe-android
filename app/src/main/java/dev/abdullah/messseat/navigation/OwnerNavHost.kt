package dev.abdullah.messseat.navigation

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBarDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import androidx.core.app.ComponentActivity
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import dev.abdullah.messseat.MainViewModel
import dev.abdullah.messseat.data.dto.property.PropertyResponse
import dev.abdullah.messseat.data.enums.NotyRefType
import dev.abdullah.messseat.domain.model.User
import dev.abdullah.messseat.features.shared.settings.SettingsScreen
import dev.abdullah.messseat.features.shared.user.UserScreen
import dev.abdullah.messseat.navigation.navtype.propertyResponseNavType
import dev.abdullah.messseat.features.owner.home.HomeScreen
import dev.abdullah.messseat.features.owner.listings.ListingsScreen
import dev.abdullah.messseat.features.owner.property_add_edit.PropertyEditorScreen
import dev.abdullah.messseat.features.owner.property_details.PropertyDetailsScreen
import dev.abdullah.messseat.features.shared.chat.ChatScreen
import dev.abdullah.messseat.features.shared.conversation.ConversationScreen
import dev.abdullah.messseat.features.shared.notification.InAppNotificationHost
import dev.abdullah.messseat.features.shared.notification.NotificationScreen
import dev.abdullah.messseat.features.shared.notification.target
import dev.abdullah.messseat.mainViewModel
import dev.chrisbanes.haze.HazeState
import dev.chrisbanes.haze.hazeSource
import kotlin.reflect.typeOf

@Composable
fun OwnerNavHost(
    modifier: Modifier = Modifier, user: User?, loggedOut: () -> Unit,
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
                    if (OwnerRoute.showBottom(currentRoute))
                        BottomNavigation(
                            navItems = OwnerRoute.TABS,
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
                    startDestination = OwnerRoute.Home,
                    // --- animation ---
                    enterTransition = enterPush(),
                    exitTransition = exitPush(),
                    popEnterTransition = enterPop(),
                    popExitTransition = exitPop()
                ) {

                    composable<OwnerRoute.Home>(
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

                    composable<OwnerRoute.Listings>(
                        enterTransition = tabEnter(), exitTransition = tabExit(),
                        popEnterTransition = tabEnter(), popExitTransition = tabExit()
                    ) {
                        ListingsScreen(
                            user = user,
                            onNavigate = ::navigate,
                            loggedOut = loggedOut
                        )
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

                    // --- Others Screens ---
                    composable<SharedRoute.User> {
                        UserScreen(
                            onBack = navController::popBackStack
                        )
                    }

                    composable<OwnerRoute.Editor>(
                        typeMap = mapOf(typeOf<PropertyResponse?>() to propertyResponseNavType)
                    ) {
                        PropertyEditorScreen(
                            updateProperty = it.toRoute<OwnerRoute.Editor>().property,
                            onBack = navController::popBackStack,
                            onGoPropertyDetails = { pId ->
                                navController.navigate(OwnerRoute.Details(pId)) {
                                    popUpTo<OwnerRoute.Editor> { inclusive = true }
                                }
                            }
                        )
                    }

                    composable<OwnerRoute.Details> {
                        val route = it.toRoute<OwnerRoute.Details>()
                        PropertyDetailsScreen(
                            propertyId = route.propertyId,
                            onEdit = { property ->
                                navController.navigate(OwnerRoute.Editor(property)) {
                                    popUpTo<OwnerRoute.Details> { inclusive = true }
                                }
                            },
                            onBack = navController::popBackStack
                        )
                    }

                    composable<SharedRoute.Conversation> {
                        ConversationScreen(onBack = navController::popBackStack)
                    }

                    composable<SharedRoute.Notification> {
                        NotificationScreen(nav = navController)
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


@Composable
fun BottomNavigation(
    navItems: Set<BottomNavItem>,
    currentRoute: String?,
    onNavigate: (NavRoute) -> Unit   // route এখন String
) {
    Card(
        modifier = Modifier
            .fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = MaterialTheme.colorScheme.onSurface
        ),
        shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            Modifier
                .fillMaxWidth()
                .windowInsetsPadding(NavigationBarDefaults.windowInsets)
                .padding(vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            navItems.forEach { item ->
                BottomNavigationItem(
                    item = item,
                    isSelected = currentRoute == item.routeName,
                    onClick = { onNavigate(item.route) }
                )
            }
        }
    }
}

@Composable
private fun BottomNavigationItem(
    item: BottomNavItem,
    isSelected: Boolean,
    onClick: () -> Unit
) {

    val mainViewModel = mainViewModel()
    val unreadChat by mainViewModel.unreadChat.collectAsStateWithLifecycle()
//    println("UnreadChat $unreadChat")

    val bgColor by animateColorAsState(
        targetValue = if (isSelected) MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)
        else Color.Transparent,
        animationSpec = tween(200),
        label = "SelectedBgColor"
    )
    val color by animateColorAsState(
        targetValue = if (isSelected) MaterialTheme.colorScheme.primary
        else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
        animationSpec = tween(200),
        label = "SelectedContentColor"
    )

    BadgedBox(
        badge = if (item.badged) {
            {

                if (unreadChat > 0) {
                    Badge {
                        Text(unreadChat.toString())
                    }
                }
            }
        } else {
            {}
        }
    ) {
        Column(
            modifier = Modifier
                .clip(RoundedCornerShape(50))
                .background(bgColor, RoundedCornerShape(50))
                .selectable(
                    selected = isSelected,
                    onClick = onClick,
                    enabled = !isSelected,
                    role = Role.Tab
                )
                .padding(horizontal = 12.dp, vertical = 6.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = item.icon,
                contentDescription = item.label,
                tint = color,
                modifier = Modifier.size(16.dp)
            )
            Text(
                text = item.label,
                style = MaterialTheme.typography.labelMedium,
                color = color
            )
        }
    }
}