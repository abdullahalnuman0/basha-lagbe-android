package dev.example.messseat.features.owner.property_add_edit

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.SnackbarDuration
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dev.example.messseat.core.presentation.components.DeleteConfirmationDialog
import dev.example.messseat.core.presentation.components.LocalSnackbar
import dev.example.messseat.core.presentation.components.StepAnimatedContent
import dev.example.messseat.core.presentation.widgets.AppTopBar
import dev.example.messseat.core.presentation.widgets.StepBottomActionBar
import dev.example.messseat.core.presentation.widgets.StepProgressIndicator
import dev.example.messseat.data.dto.property.PropertyResponse
import dev.example.messseat.features.owner.property_add_edit.components.BasicInfoSection
import dev.example.messseat.features.owner.property_add_edit.components.FacilitiesSection
import dev.example.messseat.features.owner.property_add_edit.components.LocationSection
import dev.example.messseat.features.owner.property_add_edit.components.PropertyDetailsSection
import dev.example.messseat.features.owner.property_add_edit.room_secton.RoomEditorSection
import dev.example.messseat.ui.theme.Space

@Composable
fun PropertyEditorScreen(
    modifier: Modifier = Modifier,
    updateProperty: PropertyResponse?,
    onBack: () -> Unit,
    onGoPropertyDetails: (propertyId: Long) -> Unit,
    vm: PropertyEditorViewModel = hiltViewModel()
) {

    // initiate editor state
    LaunchedEffect(updateProperty) {
        vm.initiateEditorState(updateProperty)
    }

    val snackbar = LocalSnackbar.current
    val state by vm.uiState.collectAsStateWithLifecycle()


    fun onBackLogic() {
        if (state.currentStep == PropertyStep.PROPERTY_DETAILS) {
            onBack()
            // TODO: Alert logic & back
        } else vm.onGoBack()
    }

    BackHandler { onBackLogic() }


    Column(modifier = modifier.fillMaxSize()) {
        AppTopBar(
            title = state.property?.let { "Edit Listing" } ?: "Create Listing",
            onBack = ::onBackLogic,
            actions = {
                IconButton(onClick = {
                    if (state.type == null && state.gender == null) onBack()
                    else vm.toggleDialogState()
                }) {
                    Icon(Icons.Default.Close, "Close")
                }
            },
        ) {
            Box(
                Modifier
                    .fillMaxWidth()
                    .padding(
                        start = Space.xl,
                        end = Space.xl,
                        bottom = Space.md
                    )
            ) {
                StepProgressIndicator(
                    currentStep = state.currentStep,
                    steps = state.progressSteps
                )
            }
        }

        Box(Modifier.weight(1f)) {

            StepAnimatedContent(
                targetState = state.currentStep,
                forward = state.isNavigatingForward,
                label = "PropertyStepTransition",
                modifier = Modifier
                    .fillMaxSize()
                    .padding(vertical = Space.sm, horizontal = Space.lg)
            ) { step ->
                when (step) {
                    PropertyStep.PROPERTY_DETAILS -> PropertyDetailsSection(
                        state = state,
                        onTypeSelect = vm::onTypeSelect,
                        onGenderSelect = vm::onGenderSelect
                    )

                    //Tell us the basic information about your property.
                    PropertyStep.BASIC_INFO -> BasicInfoSection(
                        state = state,
                        onTitleChange = vm::onTitleChange,
                        onDescChange = vm::onDescChange,
                        onPhoneChange = vm::onPhoneChange
                    )

                    PropertyStep.LOCATION -> LocationSection(
                        state = state,
                        onDivisionSelect = vm::onDivisionSelect,
                        onDistrictSelect = vm::onDistrictSelect,
                        onAreaChange = vm::onAreaChange,
                        onAddressChange = vm::onAddressChange,
                        onInstitutionChange = vm::onInstitutionChange
                    )

                    PropertyStep.FACILITIES -> FacilitiesSection(
                        state = state,
                        onAdd = vm::onAddFacilities,
                        onRemove = vm::onRemoveFacilities,
                        onAllRemove = vm::onAllFacilitiesRemove
                    )

                    PropertyStep.ROOMS -> RoomEditorSection(
                        state = state,
                        onAddRoom = vm::onAddRoom,
                        onRemoveRoom = vm::onRemoveRoom,
                        onUpdateRoom = vm::onUpdate
                    )
                }
            }

        }

        StepBottomActionBar(
            primaryBtnText = if (state.currentStep == state.lastStep)
                if (state.property == null) "Create property" else "Update property"
            else "Continue",
            isLoading = state.isLoading,
//            enabled = state.type != null && state.gender != null,
            showBackButton = PropertyStep.progressSteps.indexOf(state.currentStep) != 0,
            onPrimaryBtnClick = {
                vm.onGoNext(
                    onSuccess = onGoPropertyDetails,
                    onError = {
                        println("Error is: $it")
                        snackbar.show(
                            msg = it,
                            duration = SnackbarDuration.Indefinite,
                            withDismissAction = true
                        )
                    }
                )
            },
            onBackClick = ::onBackLogic
        )
    }

    if (state.showExitDialog)
        DeleteConfirmationDialog(
            title = if (state.property == null) {
                "Discard this listing?"
            } else {
                "Discard your changes?"
            },
            subtitle = if (state.property == null) {
                "Your listing hasn’t been saved yet. Are you sure you want to leave?"
            } else {
                "Your changes haven’t been saved. Are you sure you want to leave?"
            },
            confirmBtn = "Yes",
            onDismiss = vm::toggleDialogState,
            onConfirm = onBack
        )
}