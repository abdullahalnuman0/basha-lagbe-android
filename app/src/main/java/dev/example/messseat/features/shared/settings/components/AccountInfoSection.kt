package dev.example.messseat.features.shared.settings.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowForwardIos
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import dev.example.messseat.core.presentation.components.AppSpacer
import dev.example.messseat.core.presentation.components.UserAvatar
import dev.example.messseat.core.utils.toImageUrl
import dev.example.messseat.domain.model.User
import dev.example.messseat.ui.theme.Space

@Composable
fun AccountInfoSection(
    modifier: Modifier = Modifier,
    usr: User,
    onClick: () -> Unit
) {
    Column(modifier.fillMaxWidth()) {
        SectionLabel("Account")
        AppSpacer(Space.xs)
        SectionCard(onClick = onClick) {

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {

                // Avatar
                UserAvatar(url = usr.avatarUrl?.toImageUrl(), name = usr.fullName, size = 56.dp)

                Spacer(Modifier.width(16.dp))

                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = usr.fullName,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.ExtraBold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = usr.email,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                // Status
                Icon(
                    Icons.AutoMirrored.Outlined.ArrowForwardIos, "Forward",
                    Modifier
                        .size(16.dp)
                        .alpha(0.5f)
                )

            }

        }

    }
}