package dev.example.messseat.features.shared.conversation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.AddPhotoAlternate
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.example.messseat.ui.theme.Space

@Composable
fun InputSection(
    modifier: Modifier = Modifier,
    uploading: Boolean,
    sending: Boolean,
    onMessageSend: (String) -> Unit,
    onSelectImage: () -> Unit,
) {

    var text by remember { mutableStateOf("") }

    Row(
        modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surface)
            .padding(Space.sm),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        IconButton(
            enabled = !uploading,
            onClick = onSelectImage,
        ) {
            if (uploading) {
                androidx.compose.material3.CircularProgressIndicator(
                    strokeWidth = 2.dp,
                    modifier = Modifier.size(20.dp),
                )
            } else {
                Icon(
                    Icons.Filled.AddPhotoAlternate,
                    "Send an image",
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        }
        OutlinedTextField(
            value = text,
            onValueChange = { text = it },
            placeholder = { Text("Write a message…") },
            shape = MaterialTheme.shapes.extraLarge,
            maxLines = 4,
            modifier = Modifier.weight(1f),
        )
        IconButton(
            enabled = !sending && text.isNotBlank(),
            onClick = {
                onMessageSend(text)
                text = ""
            },
            modifier = Modifier
                .padding(start = Space.sm)
                .size(48.dp)
                .background(
                    if (text.isBlank()) {
                        MaterialTheme.colorScheme.surfaceContainerHighest
                    } else {
                        MaterialTheme.colorScheme.primary
                    },
                    CircleShape,
                ),
        ) {
            Icon(
                Icons.AutoMirrored.Filled.Send,
                "Send message",
                tint = if (text.isBlank()) {
                    MaterialTheme.colorScheme.onSurfaceVariant
                } else {
                    MaterialTheme.colorScheme.onPrimary
                },
            )
        }
    }

}