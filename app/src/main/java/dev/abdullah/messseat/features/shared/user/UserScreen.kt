package dev.abdullah.messseat.features.shared.user

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Logout
import androidx.compose.material.icons.outlined.Badge
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.icons.outlined.ModeEditOutline
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Phone
import androidx.compose.material.icons.outlined.School
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dev.abdullah.messseat.core.presentation.components.AppSpacer
import dev.abdullah.messseat.core.presentation.components.UserAvatar
import dev.abdullah.messseat.core.presentation.widgets.AppTopBar
import dev.abdullah.messseat.core.utils.toImageUrl
import dev.abdullah.messseat.data.enums.UserStatus
import dev.abdullah.messseat.domain.model.User
import dev.abdullah.messseat.features.shared.settings.SettingsViewModel
import dev.abdullah.messseat.ui.theme.Space

@Composable
fun UserScreen(
    modifier: Modifier = Modifier,
    onBack: () -> Unit,
    vm: SettingsViewModel = hiltViewModel()
) {

    val context = LocalContext.current
    val user by vm.user.collectAsStateWithLifecycle()

    Column(modifier.fillMaxSize()) {

        AppTopBar(
            title = "User profile",
            onBack = onBack,
            actions = {
                IconButton(onClick = {}) {
                    Icon(Icons.Outlined.ModeEditOutline, "UpdateProfile",
                        Modifier.alpha(0.6f))
                }
            }
        )


        user?.let {
            ProfileCard(
                user = it,
                loggedOut = {
                    vm.loggedOut {
                        Toast.makeText(
                            context,
                            "Successfully Logged Out.",
                            Toast.LENGTH_LONG
                        )
                            .show()
                    }
                }
            )
        }


    }
}

@Composable
fun ProfileCard(
    modifier: Modifier = Modifier,
    user: User,
    loggedOut: () -> Unit
) {

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(20.dp)
    ) {

        // Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {

            // Avatar
            UserAvatar(
                url = user.avatarUrl?.toImageUrl(),
                name = user.fullName,
                size = 56.dp
            )

            Spacer(Modifier.width(16.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = user.fullName,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.ExtraBold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = user.email,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            // Status
            Surface(
                shape = RoundedCornerShape(50),
                color = when (user.status) {
                    UserStatus.ACTIVE ->
                        MaterialTheme.colorScheme.primaryContainer

                    else ->
                        MaterialTheme.colorScheme.errorContainer
                }
            ) {
                Text(
                    text = user.status.name
                        .lowercase()
                        .replaceFirstChar { it.uppercase() },
                    modifier = Modifier.padding(
                        horizontal = 10.dp,
                        vertical = 6.dp
                    ),
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        HorizontalDivider()

        Spacer(modifier = Modifier.height(16.dp))

        // User information
        Column(
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {

            UserInfoRow(
                icon = Icons.Outlined.Email,
                label = "Email",
                value = user.email
            )

            UserInfoRow(
                icon = Icons.Outlined.Phone,
                label = "Phone",
                value = user.phone.ifBlank { "Not provided" }
            )

            UserInfoRow(
                icon = Icons.Outlined.Person,
                label = "Gender",
                value = user.gender.name
                    .lowercase()
                    .replaceFirstChar { it.uppercase() }
            )

            UserInfoRow(
                icon = Icons.Outlined.School,
                label = "Institution",
                value = user.institution ?: "Not provided"
            )

            UserInfoRow(
                icon = Icons.Outlined.LocationOn,
                label = "Location",
                value = listOfNotNull(
                    user.area,
                    user.district,
                    user.division
                ).joinToString(", ").ifBlank {
                    "Not provided"
                }
            )

            UserInfoRow(
                icon = Icons.Outlined.Badge,
                label = "Roles",
                value = user.roles.joinToString(", ") {
                    it.toString()
                        .lowercase()
                        .replaceFirstChar { c -> c.uppercase() }
                }.ifBlank {
                    "User"
                }
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        // Logout
        OutlinedButton(
            onClick = loggedOut,
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.outlinedButtonColors(
                contentColor = MaterialTheme.colorScheme.error
            ),
            border = BorderStroke(
                1.dp,
                MaterialTheme.colorScheme.error.copy(alpha = 0.5f)
            )
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Outlined.Logout,
                contentDescription = null
            )

            Spacer(modifier = Modifier.width(8.dp))

            Text(
                text = "Log out",
                fontWeight = FontWeight.Bold
            )
        }

        AppSpacer(Space.xxxl)
    }
}


@Composable
private fun UserInfoRow(
    icon: ImageVector,
    label: String,
    value: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            modifier = Modifier.size(22.dp),
            tint = MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.width(14.dp))

        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = label,
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Text(
                text = value,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Medium
            )
        }
    }
}
