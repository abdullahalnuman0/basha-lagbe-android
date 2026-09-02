package dev.abdullah.messseat.presentation.auth.signup.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
internal fun InlineErrorMessage(
    modifier: Modifier = Modifier,
    error: String?
) {
    AnimatedVisibility(
        visible = error != null,
        enter = fadeIn(tween(150)),
        exit = fadeOut(tween(150))
    ) {
        if (error != null) {
            Text(
                text = error,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall.copy(
                    fontWeight = FontWeight.Medium
                ),
                modifier = modifier.padding(start = 4.dp, top = 4.dp)
            )
        }
    }


}