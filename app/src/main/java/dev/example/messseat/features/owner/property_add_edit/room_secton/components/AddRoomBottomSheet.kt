package dev.example.messseat.features.owner.property_add_edit.room_secton.components

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.AddToPhotos
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DoneAll
import androidx.compose.material.icons.filled.EventSeat
import androidx.compose.material.icons.filled.Height
import androidx.compose.material.icons.filled.MeetingRoom
import androidx.compose.material.icons.filled.Payments
import androidx.compose.material.icons.filled.Replay
import androidx.compose.material.icons.filled.Straighten
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuAnchorType
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.ModalBottomSheetProperties
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import dev.example.messseat.BuildConfig
import dev.example.messseat.core.presentation.components.AuthInputTextField
import dev.example.messseat.core.presentation.components.InlineMessage
import dev.example.messseat.core.utils.AppProperties
import dev.example.messseat.data.enums.RoomType
import dev.example.messseat.features.owner.property_add_edit.room_secton.ImageStash
import dev.example.messseat.features.owner.property_add_edit.room_secton.RoomEditorUiState
import dev.example.messseat.ui.theme.Space
import kotlinx.coroutines.launch
import androidx.compose.material3.MaterialTheme.colorScheme as colors
import androidx.compose.material3.MaterialTheme.typography as typos

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddRoomBottomSheet(
    sheetState: SheetState,
    state: RoomEditorUiState,
    isEdit: Boolean = false,
    onRoomNumberChange: (String) -> Unit,
    onTypeSelected: (RoomType) -> Unit,
    onSeatChange: (String) -> Unit,
    onRentChange: (String) -> Unit,
    onDepositChange: (String) -> Unit,
    onWidthChange: (String) -> Unit,
    onHeightChange: (String) -> Unit,
    onUploadImages: (List<Uri>) -> Unit,
    onRemoveImage: (ImageStash) -> Unit,
    onRetry: (Int, ImageStash) -> Unit,
    onDismiss: () -> Unit,
    onSave: () -> Unit,
) {
    val scope = rememberCoroutineScope()
    val labelAlpha = 0.7f


    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickMultipleVisualMedia(maxItems = AppProperties.PROPERTY_MAX_FILE)
    ) { uris ->
        if (uris.isNotEmpty()) {
            scope.launch {
                onUploadImages(uris)
            }
        }
    }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        sheetGesturesEnabled = false,
        dragHandle = {
            Box(
                Modifier
                    .fillMaxWidth()
                    .padding(vertical = Space.xs)
            ) {
                BottomSheetDefaults.DragHandle(Modifier.align(Alignment.TopCenter))

                IconButton(
                    onClick = onDismiss,
                    enabled = !state.isLoading && !state.images.any { it.isUploading },
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(end = Space.xs)
                ) {
                    Icon(Icons.Default.Close, "Close")
                }
            }
        },
        containerColor = colors.surface,
        properties = ModalBottomSheetProperties(
            shouldDismissOnBackPress = false,
            shouldDismissOnClickOutside = false
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp)
                .padding(bottom = 32.dp)
        )
        {
            Text(
                text = if (isEdit) "Edit room" else "Add new room",
                style = typos.titleLarge,
                fontWeight = FontWeight.Bold
            )
            Spacer(Modifier.height(4.dp))
            Text(
                text = "Provide all necessary information about the room.",
                style = typos.bodyMedium,
                color = colors.onSurfaceVariant
            )

            Spacer(Modifier.height(Space.xl))

            // --- room number ---
            AuthInputTextField(
                value = state.roomNumber,
                error = state.roomNumberError,
                onValueChange = onRoomNumberChange,
                label = "Room number *",
                labelAlpha = labelAlpha,
                placeholder = "e.g. Room-102",
                icon = rememberVectorPainter(Icons.Default.MeetingRoom)
            )

            Spacer(Modifier.height(Space.lg))

            // --- Room type & seat count ---
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(Space.md)
            )
            {
                RoomTypeDropdown(
                    modifier = Modifier.weight(1f),
                    selected = state.type,
                    error = state.typeError,
                    onSelected = onTypeSelected
                )

                AuthInputTextField(
                    modifier = Modifier.weight(1f),
                    value = state.availableSeats,
                    error = state.availableSeatsError,
                    onValueChange = onSeatChange,
                    label = "Available seats *",
                    labelAlpha = labelAlpha,
                    placeholder = "e.g. 2",
                    icon = rememberVectorPainter(Icons.Default.EventSeat),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Next
                    )
                )
            }


            Spacer(Modifier.height(Space.lg))

            // ---------- Rent & Deposit ----------
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(Space.md)
            )
            {
                AuthInputTextField(
                    modifier = Modifier.weight(1f),
                    value = state.rent,
                    error = state.rentError,
                    onValueChange = onRentChange,
                    label = "Monthly rent *",
                    labelAlpha = labelAlpha,
                    placeholder = "e.g. 1000৳",
                    icon = rememberVectorPainter(Icons.Default.Payments),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Next
                    )
                )
                AuthInputTextField(
                    modifier = Modifier.weight(1f),
                    value = state.deposit,
                    onValueChange = onDepositChange,
                    label = "Deposit",
                    labelAlpha = labelAlpha,
                    placeholder = "e.g. 500৳",
                    icon = rememberVectorPainter(Icons.Default.AccountBalanceWallet),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Next
                    )
                )

            }

            Spacer(Modifier.height(Space.lg))

            // ---------- width & height ----------
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(Space.md)
            )
            {

                AuthInputTextField(
                    modifier = Modifier.weight(1f),
                    value = state.roomWidth,
                    error = state.roomWidthError,
                    onValueChange = onWidthChange,
                    label = "Room width (ft)*",
                    labelAlpha = labelAlpha,
                    placeholder = "e.g. 3",
                    icon = rememberVectorPainter(Icons.Default.Straighten),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Next
                    )
                )

                AuthInputTextField(
                    modifier = Modifier.weight(1f),
                    value = state.roomHeight,
                    error = state.roomHeightError,
                    onValueChange = onHeightChange,
                    label = "Room height (ft)*",
                    labelAlpha = labelAlpha,
                    placeholder = "e.g. 5",
                    icon = rememberVectorPainter(Icons.Default.Height),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Done
                    )
                )

            }

            Spacer(Modifier.height(Space.lg))

            // ---------- selected image ----------
            ImagePickerSection(
                images = state.images,
                error = state.imageError,
                isLoading = state.isLoading,
                onAddClick = {
                    photoPickerLauncher.launch(
                        PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                    )
                },
                onRemove = onRemoveImage,
                onRetry = onRetry
            )

            Spacer(Modifier.height(24.dp))

            // ---------- Save & close button ----------
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(Space.md)
            ) {

                TextButton(
                    onClick = onDismiss,
                    enabled = !state.isLoading && !state.images.any { it.isUploading },
                    modifier = Modifier
                        .weight(0.3f)
                        .height(48.dp),
                    shape = RoundedCornerShape(14.dp)
                ) {
                    Text("Cancel", color = colors.error)
                }

                Button(
                    onClick = onSave,
                    enabled = !state.isLoading && !state.images.any { it.isUploading },
                    modifier = Modifier
                        .weight(0.7f)
                        .height(48.dp),
                    shape = RoundedCornerShape(14.dp)
                ) {
                    Icon(Icons.Default.Check, contentDescription = null)
                    Spacer(Modifier.width(8.dp))
                    Text(
                        text = if (isEdit) "Update room" else "Save room",
                        fontSize = 16.sp
                    )
                }
            }
        }
    }
}

@Composable
private fun ImagePickerSection(
    images: List<ImageStash>,
    error: String?,
    isLoading: Boolean,
    onAddClick: () -> Unit,
    onRemove: (ImageStash) -> Unit,
    onRetry: (Int, ImageStash) -> Unit
) {
    Column {
        Text(
            text = "Room photos *",
            style = typos.labelLarge,
            fontWeight = FontWeight.Medium
        )

        error?.let {
            Spacer(Modifier.height(Space.xs))
            Text(
                text = it,
                style = typos.labelSmall.copy(
                    fontWeight = FontWeight.Normal,
                    color = colors.error
                ),
                fontWeight = FontWeight.Medium
            )
        }

        Spacer(Modifier.height(Space.sm))
        Row(
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(Space.md)
        )
        {
            Box(
                modifier = Modifier
                    .size(90.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .border(1.dp, colors.outline, RoundedCornerShape(12.dp))
                    .clickable(enabled = images.size < AppProperties.PROPERTY_MAX_FILE && !isLoading) { onAddClick() },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    if (images.size < AppProperties.PROPERTY_MAX_FILE) Icons.Default.AddToPhotos
                    else Icons.Default.DoneAll, contentDescription = "Add photo"
                )
            }

            // --- selected images
            LazyRow(horizontalArrangement = Arrangement.spacedBy(Space.md)) {

                itemsIndexed(images) { index, stash ->

                    Box(
                        modifier = Modifier
                            .width(120.dp)
                            .height(90.dp)
//                        .size(90.dp)
                            .clip(RoundedCornerShape(12.dp))
                    ) {

                        AsyncImage(
                            model = stash.fileKey?.let { "${BuildConfig.BASE_URL}files/$it" }
                                ?: stash.uri,
                            contentDescription = null,
                            modifier = Modifier
                                .fillMaxSize()
                                .alpha(if (stash.fileKey == null) 0.5f else 1f),
                            contentScale = ContentScale.Crop
                        )

                        when {
                            stash.isUploading -> CircularProgressIndicator(
                                Modifier
                                    .size(24.dp)
                                    .align(Alignment.Center),
                                color = Color.White
                            )

                            stash.error != null -> IconButton(
                                onClick = { onRetry(index, stash) },
                                modifier = Modifier
                                    .align(Alignment.Center),
                                colors = IconButtonDefaults.iconButtonColors(
                                    containerColor = colors.errorContainer,
                                    contentColor = colors.onErrorContainer
                                )
                            ) {
                                Icon(
                                    Icons.Filled.Replay,
                                    "Remove photo",
                                    Modifier.size(20.dp),
                                )
                            }
                        }


                        IconButton(
                            onClick = { onRemove(stash) },
                            modifier = Modifier
                                .padding(Space.xs, Space.xs)
                                .align(Alignment.TopEnd)
                                .size(24.dp),
                            colors = IconButtonDefaults.iconButtonColors(
                                containerColor = Color.Black.copy(alpha = 0.55f)
                            )
                        ) {
                            Icon(
                                Icons.Default.Close,
                                contentDescription = "Remove",
                                tint = Color.White,
                                modifier = Modifier.size(12.dp)
                            )
                        }
                    }
                }
            }
        }

        Spacer(Modifier.height(Space.xs))
        InlineMessage(message = "${images.filter { it.error == null && it.fileKey != null }.size} / 5 photos ` max ${AppProperties.MAX_FILE_UPLOAD_SIZE} MB each")
        Spacer(Modifier.height(Space.xs))

        // Error message show
        images.forEachIndexed { index, stash ->
            stash.error?.let { err ->
                Row(
                    horizontalArrangement = Arrangement.spacedBy(Space.xs),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        Modifier
                            .size(4.dp)
                            .background(color = Color.Gray, CircleShape)
                    )
                    InlineMessage(message = "${index + 1}) $err", isError = true)
                }
                Spacer(Modifier.height(Space.xs))
            }
        }

    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun RoomTypeDropdown(
    modifier: Modifier = Modifier,
    selected: RoomType?,
    error: String?,
    onSelected: (RoomType) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        modifier = modifier,
        expanded = expanded,
        onExpandedChange = { expanded = it }
    ) {

        AuthInputTextField(
            modifier = Modifier.menuAnchor(ExposedDropdownMenuAnchorType.PrimaryNotEditable),
            value = selected?.name ?: "",
            error = error,
            onValueChange = { },
            label = "Room type *",
            labelAlpha = 0.7f,
            placeholder = "e.g. family",
            icon = rememberVectorPainter(Icons.Default.Category),
            readOnly = true,
            isDropDown = true,
            dropDownState = expanded,
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            containerColor = colors.background
        ) {
            RoomType.entries.forEach { type ->
                DropdownMenuItem(
                    text = {
                        Text(
                            text = type.name,
                            style = typos.bodyMedium.copy(
                                fontWeight = if (selected == type) FontWeight.Bold else FontWeight.Normal,
                                color = if (selected == type) colors.primary else colors.onSurface
                            )
                        )
                    },
                    onClick = {
                        onSelected(type)
                        expanded = false
                    }
                )
            }
        }
    }
}
