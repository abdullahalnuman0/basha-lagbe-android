package dev.example.messseat.core.presentation.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.ContentTransform
import androidx.compose.animation.core.tween
import androidx.compose.animation.togetherWith
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun <T> StepAnimatedContent(
    modifier: Modifier = Modifier,
    targetState: T,
    forward: Boolean = true,
    transitionSpec: AnimatedContentTransitionScope<T>.() -> ContentTransform = {
        val enterDirection = if (forward) {
            AnimatedContentTransitionScope.SlideDirection.Left
        } else {
            AnimatedContentTransitionScope.SlideDirection.Right
        }
        val exitDirection = if (forward) {
            AnimatedContentTransitionScope.SlideDirection.Left
        } else {
            AnimatedContentTransitionScope.SlideDirection.Right
        }
        (slideIntoContainer(enterDirection, animationSpec = tween(280)))
            .togetherWith(slideOutOfContainer(exitDirection, animationSpec = tween(280)))
    },
    contentAlignment: Alignment = Alignment.TopStart,
    label: String = "AnimatedContent",
    contentKey: (targetState: T) -> Any? = { it },
    content: @Composable AnimatedContentScope.(targetState: T) -> Unit,

    ) {
    AnimatedContent(
        targetState,
        modifier,
        transitionSpec,
        contentAlignment,
        label,
        contentKey,
        content
    )

}