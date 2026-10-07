package dev.abdullah.messseat.features.owner.property_add_edit.room_secton

import android.net.Uri
import dev.abdullah.messseat.data.enums.RoomType
import dev.abdullah.messseat.domain.model.Room

data class RoomEditorUiState(
    val roomNumber: String = "",
    val rent: String = "",
    val deposit: String = "",
    val availableSeats: String = "",
    val roomWidth: String = "",
    val roomHeight: String = "",
    val type: RoomType? = null,
    val images: List<ImageStash> = emptyList(),

    val roomNumberError: String? = null,
    val rentError: String? = null,
    val availableSeatsError: String? = null,
    val roomWidthError: String? = null,
    val roomHeightError: String? = null,
    val typeError: String? = null,
    val imageError: String? = null,

    val isLoading: Boolean = false
) {
    val isValid: Boolean
        get() = roomNumberError == null && rentError == null &&
                availableSeatsError == null && roomWidthError == null &&
                roomHeightError == null && typeError == null &&
                roomNumber.isNotBlank() && rent.isNotBlank() &&
                availableSeats.isNotBlank() && roomWidth.isNotBlank() &&
                roomHeight.isNotBlank() && type != null && images.isNotEmpty()

    val toRoom: Room
        get() = Room(
            availableSeats = availableSeats.toIntOrNull() ?: 0,
            deposit = deposit.toIntOrNull() ?: 0,
            images = images.map { it.fileKey ?: "" },
            rent = rent.toIntOrNull() ?: 0,
            roomHeight = roomHeight.toIntOrNull() ?: 0,
            roomNumber = roomNumber.trim(),
            roomWidth = roomWidth.toIntOrNull() ?: 0,
            type = type,
        )
}

data class ImageStash(
    val uri: Uri, val fileKey: String? = null, val error: String? = null,
    val isUploading: Boolean = false
)