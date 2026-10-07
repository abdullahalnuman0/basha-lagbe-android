package dev.abdullah.messseat.features.tenant.home.components

import androidx.compose.foundation.layout.Box
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Wallet
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import dev.abdullah.messseat.core.presentation.components.AppDropdownMenu
import dev.abdullah.messseat.core.presentation.components.DropdownEntry
import dev.abdullah.messseat.core.presentation.components.UserAvatar
import dev.abdullah.messseat.core.utils.toImageUrl
import dev.abdullah.messseat.domain.model.User

@Composable
fun AccountDropdownOption(
    user: User?,
    size: Dp= 30.dp,
    onProfile: () -> Unit,
    loggedOut: () -> Unit
) {
    var accountOpen by remember { mutableStateOf(false) }

    Box {
        IconButton(onClick = { accountOpen = true }) {
            UserAvatar(url = user?.avatarUrl?.toImageUrl(), name = user?.fullName, size =size)
        }
        AppDropdownMenu(
            expanded = accountOpen,
            onDismiss = { accountOpen = false },
            header = {
                Text(
                    user?.fullName ?: "—",
                    style = MaterialTheme.typography.titleSmall,
                    maxLines = 1,
                )
                Text(
                    user?.email.orEmpty(),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1,
                )
            },
            entries = listOf(
                DropdownEntry("Profile", Icons.Filled.Person, onClick = onProfile),

                DropdownEntry(
                    "Log out",
                    Icons.AutoMirrored.Filled.Logout,
                    destructive = true,
                    loggedOut
                )
            ),
        )
    }
}