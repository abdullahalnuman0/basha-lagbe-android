package dev.example.messseat.features.shared.chat.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import dev.example.messseat.core.presentation.components.UserAvatar
import dev.example.messseat.core.utils.timeAgo
import dev.example.messseat.core.utils.toImageUrl
import dev.example.messseat.data.dto.chat.ChatResponse
import dev.example.messseat.features.shared.settings.components.SectionCard
import dev.example.messseat.ui.theme.Space

@Composable
fun ChatItemCard(
    modifier: Modifier = Modifier,
    chat: ChatResponse,
    onChatClick: () -> Unit
) {

    SectionCard(
        modifier,
        contentPadding = PaddingValues(0.dp),
        onClick = onChatClick
    ) {

        Row(
            Modifier
                .fillMaxWidth()
                .padding(Space.md),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            UserAvatar(
                url = chat.otherUserAvatarUrl?.toImageUrl(),
                name = chat.otherUserName,
                size = 48.dp
            )
            Column(
                Modifier
                    .weight(1f)
                    .padding(start = Space.md)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        chat.otherUserName,
                        style = MaterialTheme.typography.titleSmall,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.weight(1f),
                    )
                    Text(
                        timeAgo(chat.lastMessageAt),
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
                Row(
                    Modifier.padding(top = Space.xs),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        chat.lastMessagePreview ?: "New chat",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.weight(1f),
                    )
                    if (chat.unreadCount > 0) {
                        Surface(shape = CircleShape, color = MaterialTheme.colorScheme.primary) {
                            Text(
                                "${chat.unreadCount}",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onPrimary,
                                modifier = Modifier.padding(horizontal = Space.sm, vertical = 2.dp),
                            )
                        }
                    }
                }
            }
        }

    }
}
