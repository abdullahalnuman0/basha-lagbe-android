package dev.abdullah.messseat.features.owner.property_add_edit.room_secton

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.abdullah.messseat.core.network.ApiResult
import dev.abdullah.messseat.core.utils.AppProperties
import dev.abdullah.messseat.data.enums.RoomType
import dev.abdullah.messseat.data.repository.FileRepository
import dev.abdullah.messseat.domain.model.Room
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RoomEditorViewModel @Inject constructor(
    private val fileRepo: FileRepository
) : ViewModel() {

    private val _roomUiState = MutableStateFlow(RoomEditorUiState())
    val roomUiState = _roomUiState.asStateFlow()
    private inline fun updateState(crossinline update: RoomEditorUiState.() -> RoomEditorUiState) =
        _roomUiState.update { it.update() }

    // ====================================
    //      Room form initialization
    // ====================================
    fun initialRoom(room: Room?) {
        room?.let { initialRoom ->
            updateState {
                RoomEditorUiState(
                    roomNumber = initialRoom.roomNumber,
                    rent = if (initialRoom.rent != 0) initialRoom.rent.toString() else "",
                    deposit = if (initialRoom.deposit != 0) initialRoom.deposit.toString() else "",
                    availableSeats = if (initialRoom.availableSeats != 0) initialRoom.availableSeats.toString() else "",
                    roomWidth = if (initialRoom.roomWidth != 0) initialRoom.roomWidth.toString() else "",
                    roomHeight = if (initialRoom.roomHeight != 0) initialRoom.roomHeight.toString() else "",
                    type = initialRoom.type,
//                    images = initialRoom.images.mapNotNull { runCatching { Uri.parse(it) }.getOrNull() }
                    images = initialRoom.images.map { ImageStash(fileRepo.roomDefaultImage, it) }
                )
            }
        }
    }

    fun closeInitial(deleteImage: Boolean) {
        val fileKeys = _roomUiState.value.images.mapNotNull { it.fileKey }
        updateState { RoomEditorUiState() }

        if (deleteImage) {
            println("File delete request: -> ")
            viewModelScope.launch {
                when(val r = fileRepo.deleteAll(fileKeys)){
                    else -> println("Response $r")
                }
            }
        }
    }

    // --- room number ---
    fun onRoomNumberChange(v: String) = updateState {
        copy(roomNumber = v, roomNumberError = validateRequired(v, "Room number"))
    }

    // --- room type ---
    fun onTypeSelected(t: RoomType) = updateState {
        copy(type = t, typeError = null)
    }

    // --- room available seats ---
    fun onSeatChange(value: String) = updateState {
        copy(
            availableSeats = value,
            availableSeatsError = validatePositiveInt(value, "Available seats")
        )
    }

    // --- room rent ---
    fun onRentChange(value: String) = updateState {
        copy(rent = value, rentError = validatePositiveInt(value, "Rent"))
    }

    // --- room deposit ---
    fun onDepositChange(value: String) = updateState { copy(deposit = value) }

    // --- room width ---
    fun onWidthChange(value: String) = updateState {
        copy(roomWidth = value, roomWidthError = validatePositiveInt(value, "Room width"))
    }

    // --- room type ---
    fun onHeightChange(value: String) = updateState {
        copy(roomHeight = value, roomHeightError = validatePositiveInt(value, "Room height"))
    }

    // ====================================
    //      image upload logic
    // ====================================
    fun onUploadImages(uris: List<Uri>) = viewModelScope.launch {

        if (_roomUiState.value.isLoading) return@launch

        val currentImages = _roomUiState.value.images
        val remainingSlots = AppProperties.PROPERTY_MAX_FILE - currentImages.size

        if (remainingSlots <= 0) return@launch

        val imagesToUpload = uris.take(remainingSlots)
        val startIndex = _roomUiState.value.images.size

        updateState {
            copy(
                images = images + imagesToUpload.map { ImageStash(it, isUploading = true) },
                isLoading = true,
                imageError = null
            )
        }

        imagesToUpload.forEachIndexed { index, uri ->
//            delay(2.seconds)

            val imageIndex = startIndex + index

            when (val r = fileRepo.upload(uri, "properties")) {
                is ApiResult.Error -> updateState {
                    copy(images = images.toMutableList().apply {
                        this[imageIndex] =
                            this[imageIndex].copy(error = r.message, isUploading = false)
                    })
                }

                is ApiResult.Success -> updateState {
                    copy(images = images.toMutableList().apply {
                        this[imageIndex] =
                            this[imageIndex].copy(fileKey = r.data, isUploading = false)
                    })
                }
            }

        }

        updateState { copy(isLoading = false) }
    }

    fun onRemoveImage(value: ImageStash) {
        updateState { copy(images = images - value) }
        value.fileKey?.let { viewModelScope.launch { runCatching { fileRepo.delete(value.fileKey) } } }
    }

    fun onRetry(index: Int, stash: ImageStash) = viewModelScope.launch {

        if (_roomUiState.value.images[index].isUploading) return@launch

        updateState {
            copy(
                images = images.toMutableList().apply {
                    this[index] = this[index].copy(error = null, isUploading = true)
                }
            )
        }
//        delay(2.seconds)

        when (val r = fileRepo.upload(stash.uri, "properties")) {
            is ApiResult.Error -> updateState {
                copy(
                    images = images.toMutableList().apply {
                        this[index] = this[index].copy(error = r.message, isUploading = false)
                    }
                )
            }

            is ApiResult.Success -> updateState {
                copy(
                    images = images.toMutableList().apply {
                        this[index] = this[index].copy(fileKey = r.data, isUploading = false)
                    }
                )
            }
        }
    }


    // ====================================
    //      Add room
    // ====================================
    fun onAddRoom(onSave: (Room) -> Unit) {
        validateForm()
        if (_roomUiState.value.isValid) {
            onSave(_roomUiState.value.toRoom)
        }
    }


    // ====================================
    //      Validate form
    // ====================================
    private fun validateForm() = updateState {
        val state = _roomUiState.value
        copy(
            roomNumberError = validateRequired(state.roomNumber, "Room number"),
            rentError = validatePositiveInt(state.rent, "Rent"),
            availableSeatsError = validatePositiveInt(state.availableSeats, "Available seats"),
            roomWidthError = validatePositiveInt(state.roomWidth, "Room width"),
            roomHeightError = validatePositiveInt(state.roomHeight, "Room height"),
            typeError = if (state.type == null) "Please select room type" else null,
            imageError = if (state.images.none { it.fileKey != null }) "Please select at last one image." else null

        )
    }


    // ====================================
    //      Helper function
    // ====================================
    private fun validateRequired(value: String, fieldName: String): String? =
        if (value.isBlank()) "$fieldName is required." else null

    private fun validatePositiveInt(value: String, fieldName: String): String? {
        if (value.isBlank()) return "$fieldName is required"
        val num = value.toIntOrNull() ?: return "Give the correct number."
        if (num <= 0) return "$fieldName must be greater than 0"
        return null
    }

}