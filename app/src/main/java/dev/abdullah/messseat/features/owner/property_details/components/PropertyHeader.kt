package dev.abdullah.messseat.features.owner.property_details.components

import androidx.compose.animation.animateColor
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ErrorOutline
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.unit.dp
import dev.abdullah.messseat.core.presentation.components.ErrorBoxMessage
import dev.abdullah.messseat.domain.enums.PropertyGender
import dev.abdullah.messseat.domain.enums.PropertyType
import dev.abdullah.messseat.ui.theme.Space
import androidx.compose.material3.MaterialTheme.colorScheme as colors
import androidx.compose.material3.MaterialTheme.typography as typos

@Composable
fun PropertyHeader(
    modifier: Modifier = Modifier,
    title: String,
    approved: Boolean,
    location: String,
    type: PropertyType,
    gender: PropertyGender,
    error: String?
) {

    val seatDotColor by rememberInfiniteTransition(label = "seat_indicator").animateColor(
        initialValue = colors.primary,
        targetValue = lerp(colors.primary, colors.onSurface, 0.5f),
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = 1500,
                easing = FastOutSlowInEasing
            ),
            repeatMode = RepeatMode.Reverse
        ),
        label = "dot_color"
    )

    Surface(
        modifier = modifier.fillMaxWidth(),
        shadowElevation = 2.dp
    ) {
        Column(
            Modifier
                .fillMaxWidth()
                .padding(Space.screen),
            verticalArrangement = Arrangement.spacedBy(Space.sm)
        ) {
            ErrorBoxMessage(error = error)

            // --- tile * approved ---
            Row(
                Modifier
                    .fillMaxWidth()
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            )
            {

                Text(
                    title,
                    Modifier.weight(1f),
                    style = typos.titleLarge
                )

                Row(
                    Modifier
                        .background(
                            if (approved) colors.primaryContainer else colors.errorContainer,
                            CircleShape
                        )
                        .padding(horizontal = Space.sm, vertical = Space.xs),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(Space.xs)
                ) {

                    if (approved)
                        Box(
                            Modifier
                                .size(6.dp)
                                .background(color = seatDotColor, CircleShape)
                        )
                    else Icon(
                        Icons.Default.ErrorOutline, "Pending",
                        Modifier.size(8.dp)
                    )

                    Text(
                        if (approved) "Approved & Live" else "Pending",
                        style = typos.labelSmall
                    )
                }

            }

            // --- location ---
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(Space.xs)
            ) {
                Icon(
                    Icons.Outlined.LocationOn,
                    title,
                    Modifier.size(16.dp),
                    tint = colors.onSurface.copy(0.5f)
                )
                Text(
                    location,
                    style = typos.labelMedium.copy(
                        color = colors.onSurface.copy(0.5f)
                    )
                )
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(Space.xs)
            ) {
                HeaderChip(type.name)
                HeaderChip(gender.name)
            }

        }
    }

}

@Composable
private fun HeaderChip(
    label: String
) {
    Text(
        label,
        style = typos.labelSmall,
        color = LocalContentColor.current.copy(0.8f),
        modifier = Modifier
            .background(Color.Gray.copy(0.3f), RoundedCornerShape(4.dp))
            .padding(Space.sm, Space.xs)
    )

}