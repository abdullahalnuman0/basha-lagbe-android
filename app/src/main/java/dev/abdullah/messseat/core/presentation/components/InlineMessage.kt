package dev.abdullah.messseat.core.presentation.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun InlineMessage(
    modifier: Modifier = Modifier,
    message: String?,
    isError: Boolean? = null,
    color: Color?=null
) {
    val textColor = color ?: when{
        isError ==null -> MaterialTheme.colorScheme.onBackground
        isError -> MaterialTheme.colorScheme.error
        else -> MaterialTheme.colorScheme.primary
    }

    AnimatedVisibility(
        visible = message != null,
        enter = fadeIn(tween(150)),
        exit = fadeOut(tween(150))
    ) {
        if (message != null) {
            Text(
                text = message,
                color = textColor,
                style = MaterialTheme.typography.bodySmall.copy(
                    fontWeight = FontWeight.Medium
                ),
                modifier = modifier.padding(start = 4.dp, top = 4.dp)
            )
        }
    }


}