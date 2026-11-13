package dev.example.messseat.features.shared.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dev.example.messseat.features.shared.notification.NotificationHelper
import dev.example.messseat.features.shared.settings.components.AccountInfoSection
import dev.example.messseat.features.shared.settings.components.AppearanceSection
import dev.example.messseat.features.shared.settings.components.HeaderSection
import dev.example.messseat.features.shared.settings.components.NotificationSection
import dev.example.messseat.navigation.SharedRoute
import dev.example.messseat.navigation.NavRoute
import dev.example.messseat.ui.theme.Space

@Composable
fun SettingsScreen(
    modifier: Modifier = Modifier,
    onNavigate: (NavRoute) -> Unit,
    onRequestNotifications:()-> Unit,
    vm: SettingsViewModel = hiltViewModel()
) {

    val themeMode by vm.themeMode.collectAsStateWithLifecycle()
    val user by vm.user.collectAsStateWithLifecycle()

    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    DisposableEffect(lifecycleOwner, context, vm) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                vm.refreshNotificationStatus(
                    NotificationHelper.catPostNotifications(context)
                )
            }
        }

        lifecycleOwner.lifecycle.addObserver(observer)

        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(
            start = Space.screen,
            end = Space.screen,
            bottom = Space.xxl
        ),
        verticalArrangement = Arrangement.spacedBy(Space.md)
    ) {

        item(key = "header") { HeaderSection() }

        user?.let {
            item(key = "account") {
                AccountInfoSection(
                    usr = it,
                    onClick = { onNavigate(SharedRoute.User) })
            }
        }

        item(key = "appearance") {
            AppearanceSection(
                selectedTheme = themeMode,
                onThemeMode = vm::setThemeMode
            )
        }

        item(key = "notifications") {
            NotificationSection(
                viewModel = vm,
                onRequestNotifications = onRequestNotifications
            )
        }

    }
}