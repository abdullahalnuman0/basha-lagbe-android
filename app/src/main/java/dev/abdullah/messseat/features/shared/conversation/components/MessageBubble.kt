package dev.abdullah.messseat.features.shared.conversation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import dev.abdullah.messseat.core.utils.toImageUrl
import dev.abdullah.messseat.data.dto.chat.MessageResponse
import dev.abdullah.messseat.data.enums.MessageType
import dev.abdullah.messseat.ui.theme.Space

@Composable
fun MessageBubble(message: MessageResponse, timestamp: String, own: Boolean) {
    val isImage = message.type == MessageType.IMAGE && !message.attachmentUrl.isNullOrBlank()
    Row(
        Modifier.fillMaxWidth(),
        horizontalArrangement = if (own) Arrangement.End else Arrangement.Start,
    ) {
        Surface(
            color = if (own) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceContainerHigh,
            shape = RoundedCornerShape(
                topStart = 18.dp,
                topEnd = 18.dp,
                bottomStart = if (own) 18.dp else 4.dp,
                bottomEnd = if (own) 4.dp else 18.dp,
            ),
            modifier = Modifier.widthIn(max = 300.dp),
        ) {
            Column(Modifier.padding(horizontal = Space.md, vertical = Space.sm)) {
                if (isImage) {
                    AsyncImage(
                        model = message.attachmentUrl.toImageUrl(),
                        contentDescription = "Shared image",
                        contentScale = androidx.compose.ui.layout.ContentScale.Fit,
                        modifier = Modifier
                            .widthIn(max = 240.dp)
                            .clip(RoundedCornerShape(12.dp)),
                    )
                } else {
                    Text(
                        message.content ?: "[attachment]",
                        style = MaterialTheme.typography.bodyMedium,
                        color = if (own) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface,
                    )
                }
                Text(
                    timestamp,
                    style = MaterialTheme.typography.labelSmall,
                    color = if (own) {
                        MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.7f)
                    } else {
                        MaterialTheme.colorScheme.onSurfaceVariant
                    },
                    modifier = Modifier.padding(top = 2.dp),
                )
            }
        }
    }
}
