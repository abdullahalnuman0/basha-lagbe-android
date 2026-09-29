package dev.abdullah.messseat.core.presentation.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ErrorOutline
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme.colorScheme as colors
import androidx.compose.material3.MaterialTheme.typography as typos
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.abdullah.messseat.ui.theme.Space

@Composable
fun ErrorBoxMessage(
    modifier: Modifier = Modifier,
    error: String?
) {
    AnimatedVisibility(error != null) {
        Row(
            modifier
                .fillMaxWidth()
                .background(colors.errorContainer.copy(0.1f), RoundedCornerShape(8.dp))
                .border(
                    width = 0.1.dp,
                    color = colors.error,
                    RoundedCornerShape(8.dp)
                )
                .padding(Space.md),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(Space.md)
        ) {
            Icon(
                Icons.Default.ErrorOutline,
                error,
                Modifier.size(16.dp),
                tint = colors.error
            )
            Text(
                text = error ?: "",
                style = typos.labelMedium.copy(
                    color = colors.error
                )
            )
        }
    }
}