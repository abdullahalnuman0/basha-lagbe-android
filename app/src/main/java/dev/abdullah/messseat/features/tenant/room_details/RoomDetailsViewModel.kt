package dev.abdullah.messseat.features.tenant.room_details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.abdullah.messseat.core.data.FavoritesManager
import dev.abdullah.messseat.core.network.ApiResult
import dev.abdullah.messseat.data.repository.ChatRepository
import dev.abdullah.messseat.data.repository.TenantRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RoomDetailsViewModel @Inject constructor(
    private val tenantRepo: TenantRepository,
    private val chatRepo: ChatRepository,
    private val favorite: FavoritesManager
) : ViewModel() {

    private val _isFavorite = MutableStateFlow(false)
    val isFavorite = _isFavorite.asStateFlow()

    private val _isChatOpening = MutableStateFlow(false)
    val isChatOpening = _isChatOpening.asStateFlow()

    var roomId: Long? = null

    fun updateRoomView(id: Long) {
        viewModelScope.launch {
            roomId = id
            _isFavorite.update { favorite.isFavorite(id) }
            tenantRepo.updateRoomView(id)
        }
    }

    fun updateFavoriteState(done: (Boolean) -> Unit) {
        viewModelScope.launch {
            roomId?.let { postId ->
                if (isFavorite.value) favorite.removeFavorite(postId)
                else favorite.addFavorite(postId)
                _isFavorite.update { !isFavorite.value }
                done(isFavorite.value)
            }
        }
    }

    fun openChat(onChat: (Long) -> Unit, onError: (String) -> Unit) = viewModelScope.launch {
        _isChatOpening.update { true }
        when (val r = chatRepo.open(roomId ?: 0)) {
            is ApiResult.Success -> onChat(r.data.id)
            is ApiResult.Error -> onError(r.message)
        }
        _isChatOpening.update { false }
    }

}