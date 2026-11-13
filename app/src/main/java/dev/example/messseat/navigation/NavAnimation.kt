package dev.example.messseat.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.navigation.NavBackStackEntry

class NavAnimation {
}

private const val NAV_DURATION = 600
//private const val NAV_DURATION = 350
private const val TAB_DURATION = 250

fun enterPush(): AnimatedContentTransitionScope<NavBackStackEntry>.() -> EnterTransition = {
    slideIntoContainer(
        towards = AnimatedContentTransitionScope.SlideDirection.Left,
        animationSpec = tween(NAV_DURATION)
    ) + fadeIn(animationSpec = tween(NAV_DURATION))

}

fun exitPush(): AnimatedContentTransitionScope<NavBackStackEntry>.() -> ExitTransition = {
    slideOutOfContainer(
        towards = AnimatedContentTransitionScope.SlideDirection.Left,
        animationSpec = tween(NAV_DURATION)
    ) + fadeOut(animationSpec = tween(NAV_DURATION))
}

fun enterPop(): AnimatedContentTransitionScope<NavBackStackEntry>.() -> EnterTransition = {
    slideIntoContainer(
        towards = AnimatedContentTransitionScope.SlideDirection.Right,
        animationSpec = tween(NAV_DURATION)
    ) + fadeIn(animationSpec = tween(NAV_DURATION))
}

fun exitPop(): AnimatedContentTransitionScope<NavBackStackEntry>.() -> ExitTransition = {
    slideOutOfContainer(
        towards = AnimatedContentTransitionScope.SlideDirection.Right,
        animationSpec = tween(NAV_DURATION)
    ) + fadeOut(animationSpec = tween(NAV_DURATION))
}

private val easing = FastOutSlowInEasing

fun tabEnter():
        AnimatedContentTransitionScope<NavBackStackEntry>.() -> EnterTransition = {

    fadeIn(
        animationSpec = tween(
            durationMillis = 220,
            easing = FastOutSlowInEasing
        ),
        initialAlpha = 0.85f
    )
}

fun tabExit():
        AnimatedContentTransitionScope<NavBackStackEntry>.() -> ExitTransition = {

    fadeOut(
        animationSpec = tween(
            durationMillis = 180,
            easing = FastOutSlowInEasing
        ),
        targetAlpha = 0.95f
    )
}
