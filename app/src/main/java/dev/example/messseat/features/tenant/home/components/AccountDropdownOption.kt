package dev.example.messseat.features.tenant.home.components

import androidx.compose.foundation.layout.Box
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dev.example.messseat.core.presentation.components.AppDropdownMenu
import dev.example.messseat.core.presentation.components.DropdownEntry
import dev.example.messseat.core.presentation.components.UserAvatar
import dev.example.messseat.core.utils.toImageUrl
import dev.example.messseat.domain.model.User
import dev.example.messseat.mainViewModel

@Composable
fun AccountDropdownOption(
    user: User?,
    size: Dp = 40.dp,
    onProfile: () -> Unit,
    onNotification: (() -> Unit)? = null,
    onSaved: (() -> Unit)? = null,
    loggedOut: () -> Unit
) {
    val unreadNoty by mainViewModel().unreadNotification.collectAsStateWithLifecycle()

    var accountOpen by remember { mutableStateOf(false) }

    BadgedBox(
        badge =
            {

                if (unreadNoty > 0) {
                    Badge {
                        Text(unreadNoty.toString())
                    }
                }
            }

    ) {
        Box {
            IconButton(onClick = { accountOpen = true }) {
                UserAvatar(url = user?.avatarUrl?.toImageUrl(), name = user?.fullName, size = size)
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
                entries = listOfNotNull(
                    DropdownEntry("Profile", Icons.Filled.Person, onClick = onProfile),

                    onNotification?.let {
                        DropdownEntry(
                            "Notifications",
                            Icons.Filled.Notifications,
                            onClick = it,
                            badged = unreadNoty
                        )
                    },

                    onSaved?.let {
                        DropdownEntry(
                            "Saved posts",
                            Icons.Filled.Bookmark,
                            onClick = it
                        )
                    },

                    DropdownEntry(
                        "Log out",
                        Icons.AutoMirrored.Filled.Logout,
                        destructive = true,
                        loggedOut
                    )
                )
            )
        }
    }
}