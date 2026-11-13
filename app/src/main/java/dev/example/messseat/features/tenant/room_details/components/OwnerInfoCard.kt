package dev.example.messseat.features.tenant.room_details.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Call
import androidx.compose.material.icons.outlined.ChatBubbleOutline
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import dev.example.messseat.core.presentation.components.UserAvatar
import dev.example.messseat.core.utils.toImageUrl
import dev.example.messseat.features.shared.settings.components.SectionCard
import dev.example.messseat.ui.theme.Space

import androidx.compose.material3.MaterialTheme.colorScheme as colors
import androidx.compose.material3.MaterialTheme.typography as typos

@Composable
fun OwnerInfoCard(
    modifier: Modifier = Modifier,
    name: String,
    avatarUrl: String?,
    isChatOpening: Boolean,
    onCall: () -> Unit,
    onChat: () -> Unit
) {

    SectionCard(
        modifier = modifier,
        contentPadding = PaddingValues(0.dp)
    ) {

        Row(
            Modifier
                .padding(
                    start = Space.md,
                    end = Space.md,
                    top = Space.xxl,
                    bottom = Space.xxl
                ),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(Space.md)
        )
        {

            UserAvatar(
                url = avatarUrl?.toImageUrl(),
                name = name,
                size = 48.dp
            )

            Text(
                name,
                style = typos.headlineMedium,
                modifier = Modifier.weight(1f)
            )

            Button(
                onClick = onCall,
                shape = RoundedCornerShape(16.dp),
                contentPadding = PaddingValues(vertical = Space.md),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.DarkGray,
                    contentColor = Color.White
                )
            ) {
                Icon(Icons.Outlined.Call, "Call")
            }

            Button(
                onClick = onChat,
                shape = RoundedCornerShape(16.dp),
                contentPadding = PaddingValues(Space.md),
                colors = ButtonDefaults.buttonColors(
                    contentColor = Color.White
                )
            ) {
                if (isChatOpening) {
                    CircularProgressIndicator(Modifier.size(16.dp), color = colors.onPrimary)
                } else {
                    Icon(Icons.Outlined.ChatBubbleOutline, "Chat")
                    Spacer(Modifier.width(Space.xs))
                    Text("Chat")
                }
            }

        }

    }

}