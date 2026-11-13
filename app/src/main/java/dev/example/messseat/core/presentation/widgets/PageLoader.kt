package dev.example.messseat.core.presentation.widgets

import dev.example.messseat.core.network.ApiResult
import dev.example.messseat.data.common.PageResponse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed interface UiState<out T> {
    data object Loading : UiState<Nothing>
    data class Error(val message: String) : UiState<Nothing>
    data class Success<T>(val data: T) : UiState<T>
}

class PageLoader<T>(
    private val scope: CoroutineScope,
    private val fetch: suspend (page: Int) -> ApiResult<PageResponse<T>>
) {

    private val _state = MutableStateFlow<UiState<List<T>>>(UiState.Loading)
    val state = _state.asStateFlow()

    private val _refreshing = MutableStateFlow(false)
    val refreshing = _refreshing.asStateFlow()


    private val _appending = MutableStateFlow(false)
    val appending = _appending.asStateFlow()


    private val _error = MutableStateFlow<String?>(null)
    val error = _error.asStateFlow()

    private var page = 0
    private var last = false
    private var job: Job? = null
    val items: List<T>
        get() = (_state.value as? UiState.Success)?.data.orEmpty()
    val endReached: Boolean
        get() = last


    fun loadFirst() {
        job?.cancel()
        _state.value = UiState.Loading
        page = 0
        last = false
        job = scope.launch { fetchInto(true) }
    }

    fun refresh() {
        job?.cancel()
        _refreshing.value = true
        page = 0
        last = false
        job = scope.launch {
            fetchInto(true)
            _refreshing.value = false
        }
    }

    fun loadMore() {
        if (last || _appending.value || _refreshing.value || _state.value !is UiState.Success) return
        _appending.value = true
        scope.launch {
            page += 1
            fetchInto(false)
            _appending.value = false
        }
    }

    fun consumeError() {
        _error.value = null
    }

    fun mutate(transform: (List<T>) -> List<T>) {
        val current = _state.value
        if (current is UiState.Success)
            _state.value = UiState.Success(transform(current.data))
    }

    private suspend fun fetchInto(replace: Boolean) {
        when (val r = fetch(page)) {
            is ApiResult.Error -> {
                if (_state.value is UiState.Success) {
                    // --- keep what's on scree; report the failure
                    if (!replace) page -= 1
                    _error.value = r.message
                } else {
                    _state.value = UiState.Error(r.message)
                }
            }

            is ApiResult.Success -> {
                last = r.data.last
                val incoming = r.data.content
                _state.value = if (replace) UiState.Success(incoming)
                else UiState.Success(items + incoming)
            }
        }
    }
}