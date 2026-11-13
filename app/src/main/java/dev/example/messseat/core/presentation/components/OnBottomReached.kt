package dev.example.messseat.core.presentation.components

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.snapshotFlow
import kotlinx.coroutines.flow.distinctUntilChanged

@Composable
fun LazyListState.OnBottomReached(buffer: Int = 3, onLoadMore: () -> Unit) {
    LaunchedEffect(this) {
        snapshotFlow {
            val lastVisible = layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: -1
            (lastVisible >= layoutInfo.totalItemsCount - 1 - buffer)
                    && layoutInfo.totalItemsCount > 0
        }.distinctUntilChanged().collect { if (it) onLoadMore() }
    }
}