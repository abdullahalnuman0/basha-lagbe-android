package dev.abdullah.messseat.features.shared.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dev.abdullah.messseat.features.shared.settings.components.AccountInfoSection
import dev.abdullah.messseat.features.shared.settings.components.AppearanceSection
import dev.abdullah.messseat.features.shared.settings.components.HeaderSection
import dev.abdullah.messseat.navigation.SharedRoute
import dev.abdullah.messseat.navigation.NavRoute
import dev.abdullah.messseat.ui.theme.Space

@Composable
fun SettingsScreen(
    modifier: Modifier = Modifier,
    onNavigate: (NavRoute) -> Unit,
    vm: SettingsViewModel = hiltViewModel()
) {

    val themeMode by vm.themeMode.collectAsStateWithLifecycle()
    val user by vm.user.collectAsStateWithLifecycle()

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
            item(key = "account") { AccountInfoSection(usr = it, onClick = {onNavigate(SharedRoute.User)}) }
        }

        item(key = "appearance") {
            AppearanceSection(
                selectedTheme = themeMode,
                onThemeMode = vm::setThemeMode
            )
        }

    }
}