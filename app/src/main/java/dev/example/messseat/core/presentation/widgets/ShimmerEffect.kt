package dev.example.messseat.core.presentation.widgets

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.colorScheme as colors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import dev.example.messseat.core.presentation.components.AppCard
import dev.example.messseat.ui.theme.Space

@Composable
fun ShimmerList(
    modifier: Modifier = Modifier,
    count: Int = 6,
    row: Boolean = false,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(
                horizontal = Space.screen,
                vertical = Space.md,
            ),
        verticalArrangement = Arrangement.spacedBy(Space.listGap),
    ) {
        repeat(count) {
            if (row) ShimmerRow()
            else ShimmerCard()
        }
    }
}

@Composable
fun ShimmerCard(
    modifier: Modifier = Modifier,
) {
    AppCard(modifier.fillMaxWidth()) {
        Column(
            Modifier.padding(Space.lg),
            verticalArrangement = Arrangement.spacedBy(Space.sm),
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(Space.sm),
            ) {
                ShimmerBox(
                    modifier = Modifier.fillMaxWidth(0.22f),
                    height = 18.dp,
                    shape = MaterialTheme.shapes.small,
                )

                ShimmerBox(
                    modifier = Modifier.fillMaxWidth(0.18f),
                    height = 18.dp,
                    shape = MaterialTheme.shapes.small,
                )
            }

            ShimmerBox(
                modifier = Modifier.fillMaxWidth(0.85f),
                height = 16.dp,
            )

            ShimmerBox(
                modifier = Modifier.fillMaxWidth(0.55f),
            )

            Spacer(Modifier.height(Space.xs))

            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                ShimmerBox(
                    modifier = Modifier.fillMaxWidth(0.30f),
                )

                ShimmerBox(
                    modifier = Modifier.fillMaxWidth(0.25f),
                )
            }
        }
    }
}

@Composable
fun ShimmerRow(
    modifier: Modifier = Modifier,
    avatar: Boolean = true,
) {
    AppCard(modifier.fillMaxWidth()) {
        Row(
            Modifier.padding(Space.md),
            horizontalArrangement = Arrangement.spacedBy(Space.md),
        ) {
            if (avatar) {
                ShimmerBox(
                    modifier = Modifier.size(56.dp),
                    height = 56.dp,
                    shape = MaterialTheme.shapes.small,
                )
            }

            Column(
                Modifier
                    .padding(top = Space.xs)
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(Space.sm),
            ) {
                ShimmerBox(
                    modifier = Modifier.fillMaxWidth(0.7f),
                    height = 16.dp,
                )

                ShimmerBox(
                    modifier = Modifier.fillMaxWidth(0.45f),
                )

                ShimmerBox(
                    modifier = Modifier.fillMaxWidth(0.3f),
                )
            }
        }
    }
}

@Composable
fun ShimmerBox(
    modifier: Modifier = Modifier,
    height: Dp = 14.dp,
    shape: Shape = MaterialTheme.shapes.extraSmall,
) {
    val transition = rememberInfiniteTransition(label = "shimmer")

    val translateX by transition.animateFloat(
        initialValue = -500f,
        targetValue = 1000f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = 1100,
                easing = LinearEasing,
            ),
            repeatMode = RepeatMode.Restart,
        ),
        label = "shimmerTranslate",
    )

    val shimmerColors = listOf(
        colors.surfaceContainerHighest,
        colors.surface,
        colors.surfaceContainerHighest,
    )

    val brush = Brush.linearGradient(
        colors = shimmerColors,
        start = Offset(translateX, 0f),
        end = Offset(translateX + 250f, 0f),
    )

    Box(
        modifier = modifier
            .height(height)
            .clip(shape)
            .background(brush),
    )
}