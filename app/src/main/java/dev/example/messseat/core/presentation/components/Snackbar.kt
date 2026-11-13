package dev.example.messseat.core.presentation.components

import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.staticCompositionLocalOf
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class SnackbarController(
    val hostState: SnackbarHostState,
    private val scope: CoroutineScope
) {
    fun show(
        msg: String,
        actionLabel: String? = null,
        withDismissAction: Boolean = false,
        duration: SnackbarDuration =
            if (actionLabel == null) SnackbarDuration.Short else SnackbarDuration.Indefinite,
    ) {
        scope.launch { hostState.showSnackbar(msg, actionLabel, withDismissAction, duration) }
    }

    fun showWithAction(msg: String) =
        scope.launch { hostState.showSnackbar(msg, withDismissAction = true, duration = SnackbarDuration.Long) }
}

val LocalSnackbar = staticCompositionLocalOf<SnackbarController> {
    error("SnackbarController not provided")
}