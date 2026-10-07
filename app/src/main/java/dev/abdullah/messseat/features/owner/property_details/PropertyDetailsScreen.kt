package dev.abdullah.messseat.features.owner.property_details

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.DeleteOutline
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.EmojiObjects
import androidx.compose.material.icons.outlined.MeetingRoom
import androidx.compose.material.icons.outlined.Payment
import androidx.compose.material.icons.outlined.SingleBed
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.zIndex
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dev.abdullah.messseat.core.presentation.components.AppSpacer
import dev.abdullah.messseat.core.presentation.components.DeleteConfirmationDialog
import dev.abdullah.messseat.core.presentation.components.PullToRefresh
import dev.abdullah.messseat.core.presentation.widgets.AppTopBar
import dev.abdullah.messseat.core.presentation.widgets.ErrorBox
import dev.abdullah.messseat.core.utils.toMoney
import dev.abdullah.messseat.data.dto.property.PropertyResponse
import dev.abdullah.messseat.data.mapper.toDomain
import dev.abdullah.messseat.features.owner.property_details.components.PropertyAbout
import dev.abdullah.messseat.features.owner.property_details.components.PropertyFacilities
import dev.abdullah.messseat.features.owner.property_details.components.PropertyHeader
import dev.abdullah.messseat.features.owner.property_details.components.PropertyImageSection
import dev.abdullah.messseat.features.owner.property_details.components.PropertyLocationContact
import dev.abdullah.messseat.features.owner.property_details.components.PropertyRoomSection
import dev.abdullah.messseat.features.owner.property_details.components.PropertyStats
import dev.abdullah.messseat.features.owner.property_details.components.PropertyDetailsShimmer
import dev.abdullah.messseat.ui.theme.Space
import androidx.compose.material3.MaterialTheme.colorScheme as colors

@Composable
fun PropertyDetailsScreen(
    modifier: Modifier = Modifier,
    propertyId: Long,
    onEdit: (PropertyResponse) -> Unit,
    onBack: () -> Unit,
    vm: PropertyDetailsViewModel = hiltViewModel()
) {
    val context = LocalContext.current

    val state by vm.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(propertyId) {
        vm.loadProperty(propertyId)
    }

    fun showToast(text: String) {
        Toast.makeText(context, text, Toast.LENGTH_LONG)
            .show()
    }

    if (state.isForceLoading)
        Box(
            Modifier
                .fillMaxSize()
                .clickable(enabled = false, onClick = {})
                .zIndex(10f)
                .background(Color.DarkGray.copy(0.3f)),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }


    Column(modifier = modifier.fillMaxSize()) {
        AppTopBar(
            title = "Property details",
            onBack = onBack,
            actions = {
                Row {
                    IconButton(
                        onClick = { state.property?.let { onEdit(it) } },
                        enabled = !state.isLoading
                    ) {
                        Icon(Icons.Outlined.Edit, "Edit")
                    }

                    IconButton(
                        onClick = vm::toggleDeleteDialog,
                        enabled = !state.isLoading
                    ) {
                        Icon(
                            Icons.Outlined.DeleteOutline, "Delete",
                            tint = colors.error
                        )
                    }
                }
            }
        )


        when {
            state.property == null && state.isLoading -> PropertyDetailsShimmer()
            state.property == null && state.error != null -> ErrorBox(
                message = state.error ?: "Something want wrong!",
                onRetry = vm::loadProperty
            )

            state.property != null -> PullToRefresh(
                state.isLoading,
                vm::loadProperty
            ) {
                val property = state.property ?: return@PullToRefresh
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                        .imePadding()
                ) {


                    PropertyImageSection(
                        images = property.rooms.flatMap { it.images }
                    )

                    PropertyHeader(
                        title = property.title,
                        approved = property.approved,
                        location = "${property.area}, ${property.division}",
                        type = property.type.toDomain(),
                        gender = property.gender.toDomain(),
                        error = state.error
                    )

                    PropertyStats(
                        stats = listOf(
                            Triple(
                                "Total Rooms",
                                property.rooms.size.toString(),
                                Icons.Outlined.MeetingRoom
                            ),
                            Triple(
                                "Available Seats",
                                property.rooms.sumOf { it.availableSeats }.toString(),
                                Icons.Outlined.SingleBed
                            ),
                            Triple(
                                "Starting Rent",
                                (property.rooms.minOfOrNull { it.rent } ?: 0).toMoney(),
                                Icons.Outlined.Payment),
                            Triple(
                                "Key Facilities",
                                property.facilities.size.toString(),
                                Icons.Outlined.EmojiObjects
                            )
                        )
                    )

                    if (property.description.isNotEmpty())
                        PropertyAbout(desc = property.description)

                    AppSpacer(Space.lg)

                    PropertyFacilities(facilities = property.facilities)

                    AppSpacer(Space.lg)
                    PropertyLocationContact(
                        address = property.address,
                        institution = property.institutionNearby,
                        phone = property.contactNumber.let { if (it.startsWith("+88")) it else "+88$it" }
                    )

                    PropertyRoomSection(
                        state = state,
                        onSearchTextChange = vm::onSearchValueChange,
                        onSearchTextClear = vm::onSearchValueClear,
                        onUpdateRoom = { index, res ->
                            vm.onUpdateRoom(
                                index, res,
                                onError = ::showToast,
                                onSuccess = {
                                    showToast("Room updated successfully.")
                                }
                            )
                        },
                        onAddRoom = { res ->
                            vm.onAddRoom(
                                res,
                                onError = ::showToast,
                                onSuccess = {
                                    showToast("Room Added successfully.")
                                }
                            )
                        },
                        onAvailability = { index, newAvailability ->
                            vm.onAvailability(
                                index, newAvailability,
                                onError = ::showToast,
                                onSuccess = {
                                    showToast("Successfully Availability updated")
                                })
                        }
                    )

                    AppSpacer(Space.listBottom)

                }
            }
        }

    }



    if (state.showDeleteAlert)
        DeleteConfirmationDialog(
            onConfirm = {
                vm.deleteProperty(
                    onSuccess = {
                        showToast("Property deleted successfully.")
                        onBack()
                    },
                    onError = {
                        Toast.makeText(context, it, Toast.LENGTH_LONG).show()
                    }
                )
            },
            onDismiss = vm::toggleDeleteDialog,
        )
}