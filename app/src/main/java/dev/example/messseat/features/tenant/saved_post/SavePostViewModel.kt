package dev.example.messseat.features.tenant.saved_post

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.example.messseat.core.data.FavoritesManager
import dev.example.messseat.core.network.ApiResult
import dev.example.messseat.data.repository.TenantRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SavePostViewModel @Inject constructor(
    private val repo: TenantRepository,
    manager: FavoritesManager
) : ViewModel() {

    val roomIds = manager.favoritePostIds
        .stateIn(viewModelScope, SharingStarted.Eagerly, emptySet<String>())

    private val _uiState = MutableStateFlow(SavePostUiState())
    val uiState = _uiState.asStateFlow()


    init {
        loadSaveRooms()
    }

    fun loadSaveRooms() {
        viewModelScope.launch {
            roomIds.collectLatest { rIds ->
                if (rIds.isEmpty()) {
                    _uiState.update { SavePostUiState(isLoading = false) }
                } else {
                    when (val r = repo.getSavedRooms(rIds.map { it.toLong() })) {
                        is ApiResult.Success -> _uiState.update {
                            SavePostUiState(
                                rooms = r.data,
                                isLoading = false
                            )
                        }

                        is ApiResult.Error -> _uiState.update {
                            SavePostUiState(
                                error = r.message,
                                isLoading = true
                            )
                        }
                    }
                }
            }
        }
    }
}