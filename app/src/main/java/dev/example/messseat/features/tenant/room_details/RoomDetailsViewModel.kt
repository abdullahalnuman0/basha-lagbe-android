package dev.example.messseat.features.tenant.room_details

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.example.messseat.core.data.FavoritesManager
import dev.example.messseat.core.network.ApiResult
import dev.example.messseat.data.repository.ChatRepository
import dev.example.messseat.data.repository.TenantRepository
import dev.example.messseat.navigation.TenantRoute
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RoomDetailsViewModel @Inject constructor(
    private val tenantRepo: TenantRepository,
    private val chatRepo: ChatRepository,
    private val favorite: FavoritesManager,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val args = checkNotNull(savedStateHandle.toRoute<TenantRoute.RoomDetails>())

    private val _uiState = MutableStateFlow(RoomDetailsUiState())
    val uiState = _uiState.asStateFlow()


    init {
       loadRoom()
    }

    fun loadRoom(){
        viewModelScope.launch {
            when (val r = tenantRepo.findRoom(args.roomId)) {
                is ApiResult.Success -> {
                    _uiState.value = RoomDetailsUiState(
                        roomId = args.roomId,
                        room = r.data,
                        isFavorite = favorite.isFavorite(args.roomId),
                        isLoading = false
                    )
                }

                is ApiResult.Error -> {
                    _uiState.value = RoomDetailsUiState(
                        roomId = args.roomId,
                        error = r.message,
                        isLoading = false
                    )
                }
            }
        }
    }


    fun updateFavoriteState(done: (Boolean) -> Unit) {
        viewModelScope.launch {
            _uiState.value.let { state ->
                if (state.isFavorite) favorite.removeFavorite(state.roomId)
                else favorite.addFavorite(state.roomId)
                done(!state.isFavorite)
                _uiState.update { it.copy(isFavorite = !state.isFavorite) }
            }
        }
    }

    fun openChat(onChat: (Long) -> Unit, onError: (String) -> Unit) = viewModelScope.launch {
        _uiState.update { it.copy(isChatOpening = true) }
        when (val r = chatRepo.open(_uiState.value.roomId)) {
            is ApiResult.Success -> onChat(r.data.id)
            is ApiResult.Error -> onError(r.message)
        }
        _uiState.update { it.copy(isChatOpening = true) }
    }

}