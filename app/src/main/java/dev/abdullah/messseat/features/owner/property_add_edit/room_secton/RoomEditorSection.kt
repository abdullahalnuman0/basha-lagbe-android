package dev.abdullah.messseat.features.owner.property_add_edit.room_secton

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dev.abdullah.messseat.core.presentation.widgets.PageHeader
import dev.abdullah.messseat.domain.model.Room
import dev.abdullah.messseat.features.auth.signup.components.dashedBorder
import dev.abdullah.messseat.features.owner.property_add_edit.PropertyEditorUiState
import dev.abdullah.messseat.core.presentation.components.ErrorBoxMessage
import dev.abdullah.messseat.features.owner.property_add_edit.room_secton.components.AddRoomBottomSheet
import dev.abdullah.messseat.features.owner.property_add_edit.room_secton.components.RoomViewerCard
import dev.abdullah.messseat.ui.theme.Space
import kotlinx.coroutines.launch
import androidx.compose.material3.MaterialTheme.colorScheme as colors
import androidx.compose.material3.MaterialTheme.typography as typos

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RoomEditorSection(
    modifier: Modifier = Modifier,
    state: PropertyEditorUiState,
    onAddRoom: (Room) -> Unit,
    onRemoveRoom: (Room) -> Unit,
    onUpdateRoom: (Int, Room) -> Unit,
    roomVm: RoomEditorViewModel = hiltViewModel()
) {

    val roomState by roomVm.roomUiState.collectAsStateWithLifecycle()

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

    LaunchedEffect(editingRoom) {
        roomVm.initialRoom(editingRoom?.second)
    }

    LazyColumn(
        modifier
            .fillMaxWidth()
            .imePadding()
    ) {

        item {
            Spacer(Modifier.height(Space.md))

            PageHeader(
                title = "Available rooms",
                subtitle = "Add the rooms available in your hostel, flat or mess. Configure pricing, deposit and upload room photos",
                titleStyle = typos.headlineMedium,
                subtitleStyle = typos.bodyMedium.copy(
                    color = colors.onBackground.copy(0.7f)
                ),
            )

            Spacer(Modifier.height(Space.xxxl))
        }

        item {
            ErrorBoxMessage(
                modifier = Modifier.padding(bottom = Space.xxxl),
                error = state.roomsError
            )
        }


        itemsIndexed(state.rooms) { index, room ->
            RoomViewerCard(
                room = room,
                onEdit = {
                    editingRoom = (index to room)
                    showSheet = true
                },
                onDelete = { onRemoveRoom(room) }
            )
            Spacer(Modifier.height(Space.lg))
        }

        item {
            AddRoomBtn {
                editingRoom = null
                showSheet = true
            }

            Spacer(Modifier.height(Space.xxxl))
        }
    }

    if (showSheet) {
        AddRoomBottomSheet(
            sheetState = sheetState,
            state = roomState,
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
            onDismiss = { closeSheet(true) },
            onSave = {
                roomVm.onAddRoom { room ->
                    editingRoom?.let { onUpdateRoom(it.first, room.copy(id = it.second.id)) }
                        ?: onAddRoom(room)

                    closeSheet(false)
                }
            }
        )
    }
}


// ====================================
// Room Add button
// ====================================
@Composable
private fun AddRoomBtn(
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .dashedBorder(
                color = colors.outline,
                strokeWidth = 1.dp,
                cornerRadius = 16.dp
            ),
        colors = CardDefaults.cardColors(
            containerColor = colors.surface,
            contentColor = colors.onSurface
        ),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            Modifier
                .fillMaxWidth()
                .padding(vertical = Space.xl),
            verticalArrangement = Arrangement.spacedBy(Space.xs),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Box(
                Modifier
                    .size(40.dp)
                    .background(colors.primaryContainer, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Default.Add, "Add")
            }

            Text("Add Room", style = typos.headlineSmall.copy(color = colors.primary))
            Text("Tap to add another room", style = typos.bodyMedium)
        }
    }
}
