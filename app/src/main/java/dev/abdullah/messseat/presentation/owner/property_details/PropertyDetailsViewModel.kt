package dev.abdullah.messseat.presentation.owner.property_details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.abdullah.messseat.core.network.ApiResult
import dev.abdullah.messseat.data.dto.room.RoomAvailabilityRequest
import dev.abdullah.messseat.data.dto.room.RoomRequest
import dev.abdullah.messseat.data.dto.room.RoomResponse
import dev.abdullah.messseat.data.repository.PropertyRepository
import dev.abdullah.messseat.domain.model.Room
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.time.Duration.Companion.seconds

@HiltViewModel
class PropertyDetailsViewModel @Inject constructor(
    private val propertyRepo: PropertyRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(PropertyDetailsUiState())
    val uiState = _uiState.asStateFlow()


    fun loadProperty(propertyId: Long? = null) {
        val id = propertyId ?: _uiState.value.propertyId

        if (id == null) {
            _uiState.update {
                it.copy(
                    isLoading = false,
                    error = "Don't have any ID."
                )
            }
            return
        }

        _uiState.update {
            it.copy(
                propertyId = id,
                isLoading = true,
                error = null
            )
        }

        viewModelScope.launch {
            when (val result = propertyRepo.findOne(id)) {
                is ApiResult.Error -> {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            error = result.message
                        )
                    }
                }

                is ApiResult.Success -> {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            error = null,
                            property = result.data
                        )
                    }
                }
            }
        }
    }


    fun toggleDeleteDialog(v: Boolean? = null) {
        val state = v ?: !_uiState.value.showDeleteAlert
        _uiState.update { it.copy(showDeleteAlert = state) }
    }

    fun deleteProperty(onSuccess: () -> Unit, onError: (String) -> Unit) {
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    isLoading = true,
                    showDeleteAlert = false
                )
            }

            when (val r = propertyRepo.deleteProperty(_uiState.value.propertyId!!)) {
                is ApiResult.Error -> {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            error = r.message
                        )
                    }
                    onError(r.message)
                }

                is ApiResult.Success<*> -> {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                        )
                    }
                    onSuccess()
                }
            }


        }
    }


    fun onSearchValueChange(v: String) = _uiState.update { it.copy(searchText = v) }
    fun onSearchValueClear() = _uiState.update { it.copy(searchText = "") }

    fun onUpdateRoom(
        index: Int,
        room: RoomResponse,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        viewModelScope.launch {
            _uiState.update { it.copy(isForceLoading = true) }

            val requestBody = RoomRequest(
                room.availableSeats,
                room.deposit,
                room.images,
                room.rent,
                room.roomHeight,
                room.roomNumber,
                room.roomWidth,
                room.type,
            )
            when (val r =
                propertyRepo.updateRoom(_uiState.value.property!!.rooms[index].id, requestBody)) {
                is ApiResult.Error -> {
                    _uiState.update { it.copy(isForceLoading = false) }
                    onError(r.message)
                }

                is ApiResult.Success -> {
                    val property = _uiState.value.property?.let { property ->
                        property.copy(
                            rooms = property.rooms.toMutableList().apply {
                                this[index] = r.data
                            }
                        )
                    }

                    _uiState.update {
                        it.copy(property = property, isForceLoading = false)
                    }

                    onSuccess()
                }
            }


        }
    }

    fun onAddRoom(room: Room, onSuccess: () -> Unit, onError: (String) -> Unit) {

        viewModelScope.launch {
            _uiState.update { it.copy(isForceLoading = true) }

            val requestBody = RoomRequest(
                room.availableSeats,
                room.deposit,
                room.images,
                room.rent,
                room.roomHeight,
                room.roomNumber,
                room.roomWidth,
                room.type!!,
            )

            when (val r = propertyRepo.createRoom(_uiState.value.propertyId!!, requestBody)) {
                is ApiResult.Error -> {
                    _uiState.update { it.copy(isForceLoading = false) }
                    onError(r.message)
                }

                is ApiResult.Success -> {
                    val property = _uiState.value.property?.let { property ->
                        property.copy(
                            rooms = property.rooms.toMutableList().apply {
                                add(r.data)
                            }
                        )
                    }
                    _uiState.update {
                        it.copy(
                            property = property,
                            isForceLoading = false
                        )
                    }

                    onSuccess()
                }
            }
        }


    }

    fun onAvailability(
        index: Int,
        newAvailability: Int,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        viewModelScope.launch {
            _uiState.update { it.copy(isForceLoading = true) }

            val availability = if (newAvailability <= -1) 0 else newAvailability

            val request =
                RoomAvailabilityRequest(_uiState.value.property!!.rooms[index].id, availability)

            when (val r = propertyRepo.roomAvailability(request)) {
                is ApiResult.Error -> {
                    _uiState.update { it.copy(isForceLoading = false) }
                    onError(r.message)
                }

                is ApiResult.Success<*> -> {
                    val property = _uiState.value.property?.let { property ->
                        property.copy(
                            rooms = property.rooms.toMutableList().apply {
                                this[index] = this[index].copy(availableSeats = availability)
                            }
                        )
                    }

                    _uiState.update {
                        it.copy(property = property, isForceLoading = false)
                    }

                    onSuccess()
                }
            }

        }
    }

}