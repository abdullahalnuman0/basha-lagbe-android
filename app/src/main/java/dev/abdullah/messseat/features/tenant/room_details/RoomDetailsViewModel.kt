package dev.abdullah.messseat.features.tenant.room_details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.abdullah.messseat.core.data.FavoritesManager
import dev.abdullah.messseat.data.repository.TenantRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RoomDetailsViewModel @Inject constructor(
    private val repo: TenantRepository,
    private val favorite: FavoritesManager
) : ViewModel() {

    private val _isFavorite = MutableStateFlow(false)
    val isFavorite = _isFavorite.asStateFlow()
    var roomId: Long? = null

    fun updateRoomView(id: Long) {
        viewModelScope.launch {
            roomId = id
            _isFavorite.update { favorite.isFavorite(id) }
            repo.updateRoomView(id)
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

}