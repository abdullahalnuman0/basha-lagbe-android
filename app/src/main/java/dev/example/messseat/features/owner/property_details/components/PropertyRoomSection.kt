package dev.example.messseat.features.owner.property_details.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SheetValue
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dev.example.messseat.data.dto.room.RoomResponse
import dev.example.messseat.domain.model.Room
import dev.example.messseat.features.owner.property_add_edit.room_secton.RoomEditorViewModel
import dev.example.messseat.features.owner.property_add_edit.room_secton.components.AddRoomBottomSheet
import dev.example.messseat.features.owner.property_details.PropertyDetailsUiState
import dev.example.messseat.features.owner.property_details.components.room.AvailabilityDialog
import dev.example.messseat.features.owner.property_details.components.room.RoomDetailsList
import dev.example.messseat.features.owner.property_details.components.room.RoomHeader
import dev.example.messseat.ui.theme.Space
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PropertyRoomSection(
    modifier: Modifier = Modifier,
    state: PropertyDetailsUiState,
    onSearchTextChange: (String) -> Unit,
    onSearchTextClear: () -> Unit,
    onUpdateRoom: (Int, RoomResponse) -> Unit,
    onAddRoom: (Room) -> Unit,
    onAvailability: (roomIndex: Int, newAvailability: Int) -> Unit,
    roomVm: RoomEditorViewModel = hiltViewModel()
) {
    val property = state.property ?: return

    val roomEditorState by roomVm.roomUiState.collectAsStateWithLifecycle()

    var showAvailabilityDialog by remember { mutableStateOf(false) }
    var availabilityRoom by remember { mutableStateOf<Pair<Int, Room>?>(null) }

    var showSheet by remember { mutableStateOf(false) }
    var editingRoom by remember { mutableStateOf<Pair<Int, Room>?>(null) }
    var allowDismiss by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true,
        confirmValueChange = { tv ->
            if (tv == SheetValue.Hidden) allowDismiss else true
        })
    val scope = rememberCoroutineScope()

    fun closeSheet(deleteImages: Boolean) {
        allowDismiss = true
        scope.launch { sheetState.hide() }.invokeOnCompletion {
            showSheet = false
            allowDismiss = false

            // rest the ui state
            roomVm.closeInitial(deleteImages)
        }

    }


    val filteredRooms = remember(state.searchText, property.rooms) {
        if (state.searchText.isBlank()) {
            property.rooms
        } else {
            property.rooms.filter { room ->
                room.roomNumber.contains(
                    other = state.searchText.trim(),
                    ignoreCase = true
                )
            }
        }
    }

    Column(
        modifier
            .fillMaxWidth()
            .padding(Space.screen),
        verticalArrangement = Arrangement.spacedBy(Space.listGap)
    ) {

        RoomHeader(
            totalRoom = state.property.rooms.size,
            searchText = state.searchText,
            onAdd = {
                editingRoom = null
                showSheet = true
            },
            onSearchChange = onSearchTextChange,
            onSearchClear = onSearchTextClear,
        )

        RoomDetailsList(
            searchText = state.searchText, rooms = filteredRooms,
            onEdit = {
                editingRoom = it to property.rooms[it].toRoom()
                roomVm.initialRoom(editingRoom?.second)
                showSheet = true
            },
            onAvailability = {
                availabilityRoom = it to property.rooms[it].toRoom()
                showAvailabilityDialog = true
            }
        )
    }


    if (showSheet) {
        AddRoomBottomSheet(
            sheetState = sheetState,
            state = roomEditorState,
            isEdit = editingRoom != null,
            onRoomNumberChange = roomVm::onRoomNumberChange,
            onTypeSelected = roomVm::onTypeSelected,
            onSeatChange = roomVm::onSeatChange,
            onRentChange = roomVm::onRentChange,
            onDepositChange = roomVm::onDepositChange,
            onWidthChange = roomVm::onWidthChange,
            onHeightChange = roomVm::onHeightChange,
            onUploadImages = roomVm::onUploadImages,
            onRemoveImage = roomVm::onRemoveImage,
            onRetry = roomVm::onRetry,
            onDismiss = {closeSheet(true)},
            onSave = {
                roomVm.onAddRoom { room ->
                    editingRoom?.let { onUpdateRoom(it.first, room.toRoomResponse()) }
                        ?: onAddRoom(room)
                    closeSheet(false)
                }
            }
        )
    }

    if (showAvailabilityDialog && availabilityRoom != null) {
        AvailabilityDialog(
            roomNumber = availabilityRoom!!.second.roomNumber,
            availableSeats = availabilityRoom!!.second.availableSeats,
            onDismiss = {
                availabilityRoom = null
                showAvailabilityDialog = false
            },
            onSave = {
                onAvailability(availabilityRoom!!.first, it)
                availabilityRoom = null
                showAvailabilityDialog = false
            }
        )
    }

}