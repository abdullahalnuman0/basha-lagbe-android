package dev.example.messseat.features.shared.notification


import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Notifications
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.liveRegion
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import dev.example.messseat.data.dto.notification.NotificationResponse
import dev.chrisbanes.haze.HazeInput
import dev.chrisbanes.haze.HazeState
import dev.chrisbanes.haze.blur.HazeBlurStyle
import dev.chrisbanes.haze.blur.HazeColorEffect
import dev.chrisbanes.haze.blur.hazeBlur
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.abs
import kotlin.math.roundToInt
import kotlin.math.sign
import kotlin.time.Duration.Companion.seconds

private val AUTO_DISMISS_S = 5.seconds

/**
 * অ্যাপের একদম উপরে ভাসমান গ্লাস নোটিফিকেশন ব্যানার।
 *
 * - [hazeState] অবশ্যই এমন একটা Box/Composable এ `Modifier.hazeSource(hazeState)` দিয়ে বাঁধা থাকতে হবে
 *   যেটা ব্যানারের "পেছনের" কনটেন্ট (মানে পুরো অ্যাপ UI) ধারণ করে।
 * - ব্যানারে ক্লিক করলে [onClick], সোয়াইপ/টাইমআউটে [onDismiss] কল হবে।
 */
@Composable
fun InAppNotificationHost(
    banner: NotificationResponse?,
    hazeState: HazeState,
    onClick: (NotificationResponse) -> Unit,
    onDismiss: (id: Long) -> Unit,
    modifier: Modifier = Modifier,
) {
    AnimatedContent(
        targetState = banner,
        modifier = modifier
            .fillMaxWidth()
            .statusBarsPadding()
            .padding(horizontal = 12.dp, vertical = 8.dp),
        transitionSpec = {
            (slideInVertically(
                animationSpec = spring(
                    dampingRatio = 0.75f,
                    stiffness = Spring.StiffnessMediumLow
                )
            ) { -it * 2 } + fadeIn(tween(200))) togetherWith
                    (slideOutVertically(tween(220)) { -it * 2 } + fadeOut(tween(180))) using
                    SizeTransform(clip = false) // clip=false না দিলে স্লাইড-ইন কেটে যায়
        },
        contentAlignment = Alignment.TopCenter,
        label = "inAppNotification",
    ) { noty ->
        if (noty != null) {
            NotificationBanner(
                noty = noty,
                hazeState = hazeState,
                onClick = onClick,
                onDismiss = onDismiss,
            )
        }
    }
}

@Composable
private fun NotificationBanner(
    noty: NotificationResponse,
    hazeState: HazeState,
    onClick: (NotificationResponse) -> Unit,
    onDismiss: (Long) -> Unit,
) {
    val scope = rememberCoroutineScope()
    val haptics = LocalHapticFeedback.current
    val colors = MaterialTheme.colorScheme

    val offsetX = remember { Animatable(0f) }
    val offsetY = remember { Animatable(0f) }
    var touching by remember { mutableStateOf(false) }

    val shape = RoundedCornerShape(22.dp)
    val glassStyle = remember(colors.onBackground) {
        HazeBlurStyle {
            backgroundColor(colors.onBackground)
            colorEffects(
                listOf(
                    HazeColorEffect.tint(
                        colors.onBackground.copy(alpha = 0.55f)
                    )
                )
            )
            blurRadius(24.dp)
            noiseFactor(0.04f)
        }
    }
    /*val glassStyle = remember(colors.surface) {
        HazeBlurStyle {
            backgroundColor(colors.surface.copy(alpha = 0.80f))
            colorEffects(
                listOf(
                    HazeColorEffect.tint(
                        colors.surface.copy(alpha = 0.65f)
                    )
                )
            )
            blurRadius(24.dp)
            noiseFactor(0.04f)
        }
    }*/

    // দেখানোর সময় হালকা ভাইব্রেশন
    LaunchedEffect(noty.id) {
        haptics.performHapticFeedback(HapticFeedbackType.TextHandleMove)
    }

    // অটো-ডিসমিস: আঙুল দিয়ে ধরে রাখলে টাইমার থামে
    LaunchedEffect(noty.id, touching) {
        if (!touching) {
            delay(AUTO_DISMISS_S)
            onDismiss(noty.id)
        }
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .offset { IntOffset(offsetX.value.roundToInt(), offsetY.value.roundToInt()) }
            .graphicsLayer {
                // সাইডে সরালে ধীরে ধীরে ফেড হবে
                val progress = (abs(offsetX.value) / size.width).coerceIn(0f, 1f)
                alpha = 1f - progress * 0.8f
            }
            .clip(shape)
            .hazeBlur(
                input = HazeInput.Sources(hazeState),
                style = glassStyle,
            )
            .border(
                width = 1.dp,
                brush = Brush.verticalGradient(
                    listOf(Color.White.copy(alpha = 0.55f), Color.White.copy(alpha = 0.10f))
                ),
                shape = shape,
            )
            .semantics(mergeDescendants = true) { liveRegion = LiveRegionMode.Polite }
            .pointerInput(noty.id) {
                val dismissX = size.width * 0.35f
                val dismissY = 48.dp.toPx()

                fun settle() {
                    scope.launch {
                        when {
                            abs(offsetX.value) > dismissX -> {
                                offsetX.animateTo(
                                    sign(offsetX.value) * size.width * 1.2f,
                                    tween(150)
                                )
                                onDismiss(noty.id)
                            }

                            offsetY.value < -dismissY -> {
                                offsetY.animateTo(-(size.height + 200f), tween(150))
                                onDismiss(noty.id)
                            }

                            else -> {
                                launch { offsetX.animateTo(0f, spring()) }
                                launch { offsetY.animateTo(0f, spring()) }
                            }
                        }
                    }
                }

                detectDragGestures(
                    onDragStart = { touching = true },
                    onDragEnd = { touching = false; settle() },
                    onDragCancel = { touching = false; settle() },
                    onDrag = { change, drag ->
                        change.consume()
                        scope.launch {
                            offsetX.snapTo(offsetX.value + drag.x)
                            // উপরের দিকে টানা যাবে, নিচে নয়
                            offsetY.snapTo((offsetY.value + drag.y).coerceAtMost(0f))
                        }
                    },
                )
            }
            .clickable { onClick(noty) }
            .padding(horizontal = 14.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Box(
            modifier = Modifier
                .size(38.dp)
                .clip(CircleShape)
                .background(colors.primary.copy(alpha = 0.18f)),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                imageVector = Icons.Rounded.Notifications,
                contentDescription = null,
                tint = colors.primary,
            )
        }

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = noty.title,
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.SemiBold,
                color = colors.background,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
            noty.body?.takeIf { it.isNotBlank() }?.let { body ->
                Text(
                    text = body,
                    style = MaterialTheme.typography.bodySmall,
                    color = colors.background.copy(alpha = 0.75f),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                )
            }
        }
    }
}