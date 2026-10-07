package dev.abdullah.messseat.core.presentation.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dev.abdullah.messseat.core.presentation.widgets.ErrorBox
import dev.abdullah.messseat.core.presentation.widgets.PageLoader
import dev.abdullah.messseat.core.presentation.widgets.UiState

@Composable
fun PullToRefresh(
    refreshing: Boolean,
    onRefresh: () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable BoxScope.() -> Unit
) {
    PullToRefreshBox(
        refreshing,
        onRefresh,
        modifier.fillMaxSize()
    ) { content() }
}

@Composable
fun <T> LoaderContent(
    loader: PageLoader<T>,
    modifier: Modifier = Modifier,
    skeleton: (@Composable () -> Unit)? = null,
    content: @Composable (T) -> Unit,
) {
    val state by loader.state.collectAsStateWithLifecycle()
    val refreshing by loader.refreshing.collectAsStateWithLifecycle()
    val error by loader.error.collectAsStateWithLifecycle()
    val snackbar = LocalSnackbar.current

    LaunchedEffect(error) {
        error?.let {
            snackbar.show(it)
            loader.consumeError()
        }
    }

    Box(modifier.fillMaxSize()) {
        when (state) {
            is UiState.Loading -> if (skeleton != null) skeleton() else CircularProgressIndicator()
            is UiState.Error -> ErrorBox(message = (state as UiState.Error).message, onRetry = loader::loadFirst)
            is UiState.Success -> PullToRefresh(
                refreshing,
                loader::refresh
            ) {
                content((state as UiState.Success<T>).data)
            }
        }
    }
}
