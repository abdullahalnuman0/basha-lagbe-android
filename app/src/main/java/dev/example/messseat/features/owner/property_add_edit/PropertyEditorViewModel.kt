package dev.example.messseat.features.owner.property_add_edit

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.example.messseat.core.network.ApiResult
import dev.example.messseat.data.dto.property.PropertyRequest
import dev.example.messseat.data.dto.property.PropertyResponse
import dev.example.messseat.data.dto.property.PropertyUpdateRequest
import dev.example.messseat.data.dto.room.RoomRequest
import dev.example.messseat.data.mapper.toData
import dev.example.messseat.data.mapper.toDomain
import dev.example.messseat.data.repository.FileRepository
import dev.example.messseat.data.repository.PropertyRepository
import dev.example.messseat.domain.enums.PropertyGender
import dev.example.messseat.domain.enums.PropertyType
import dev.example.messseat.domain.model.Room
import dev.example.messseat.features.auth.signup.LocationData
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PropertyEditorViewModel @Inject constructor(
    private val fileRepo: FileRepository,
    private val propertyRepo: PropertyRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(
        PropertyEditorUiState(
//            currentStep = PropertyStep.ROOMS,
//            type = PropertyType.MESS,
//            gender = PropertyGender.ANY,
//            contactNumber = "01580685647",
//            title = "Green View Hostel",
//            description = "Well-ventilated quiet student hostel located within walking distance of JUST",
//            division = LocationData.divisions[0],
//            district = LocationData.districtByDivision[LocationData.divisions[0]]!![0],
//            area = LocationData.popularAreasByDistrict[LocationData.districtByDivision[LocationData.divisions[0]]!![0]]!![0],
//            address = "24/4 A, Saha road",
//            institutionNearby = LocationData.popularInstitutions[0],
//            facilities = setOf("Wi-Fi", "Parking", "Water Supply")
        )
    )
    val uiState = _uiState.asStateFlow()

    private inline fun updateState(crossinline update: PropertyEditorUiState.() -> PropertyEditorUiState) =
        _uiState.update { it.update() }

    //---------------------------------
    //    Initiate Editor State
    //---------------------------------
    fun initiateEditorState(property: PropertyResponse?) {
        property?.let { p ->
            _uiState.update {
                PropertyEditorUiState(
                    progressSteps = PropertyStep.updateProgressSteps,
                    currentStep = PropertyStep.updateProgressSteps[0],
                    lastStep = PropertyStep.updateProgressSteps.last(),

                    property = p,

                    type = p.type.toDomain(),
                    gender = p.gender.toDomain(),

                    title = p.title,
                    description = p.description,
                    contactNumber = p.contactNumber.takeLast(11),

                    division = p.division,
                    district = p.district,
                    area = p.area,
                    address = p.address,
                    institutionNearby = p.institutionNearby,

                    facilities = p.facilities.toSet()
                )
            }
        }
    }

    // --- toggle dialog state ---
    fun toggleDialogState(v: Boolean?=null){
        val state = v ?: !_uiState.value.showExitDialog
        updateState { copy(showExitDialog = state) }
    }

    // --- basic info ---
    fun onTitleChange(value: String) = updateState { copy(title = value, titleError = null) }
    fun onDescChange(value: String) = updateState { copy(description = value.take(500)) }
    fun onPhoneChange(value: String) = updateState {
        copy(
            contactNumber = value.filter { it.isDigit() }.take(11),
            contactNumberError = null
        )
    }

    private fun validateBasicInfo(): Boolean {
        val state = _uiState.value

        var titleError: String? = null
        var phoneError: String? = null
        val phoneRegex = Regex("^01[3-9]\\d{8}$")

        if (state.title.trim().isEmpty())
            titleError = "Please enter a valid listing title. "

        if (!phoneRegex.matches(state.contactNumber))
            phoneError = "Enter a valid phone number."

        updateState { copy(titleError = titleError, contactNumberError = phoneError) }

        return titleError == null && phoneError == null
    }


    // --- location ---
    fun onDivisionSelect(value: String) =
        updateState {
            copy(
                division = value,
                district = LocationData.districtByDivision[value]!![0],
                divisionError = null
            )
        }

    fun onDistrictSelect(value: String) =
        updateState { copy(district = value, districtError = null) }

    fun onAreaChange(value: String) = updateState { copy(area = value, areaError = null) }
    fun onAddressChange(value: String) = updateState { copy(address = value, addressError = null) }
    fun onInstitutionChange(value: String) = updateState { copy(institutionNearby = value) }
    fun validateLocation(): Boolean {

        val state = _uiState.value

        var areaError: String? = null
        var addressError: String? = null

        if (state.area.trim().isEmpty())
            areaError = "Enter your property area"
        if (state.address.trim().isEmpty())
            addressError = "Enter your property address."

        updateState {
            copy(
                areaError = areaError,
                addressError = addressError,
                area = state.area.trim(),
                address = state.address.trim()
            )
        }

        return areaError == null && addressError == null
    }

    // --- property details ---
    fun onTypeSelect(type: PropertyType) =
        updateState { copy(type = type, propertyDetailsError = null) }

    fun onGenderSelect(gender: PropertyGender) =
        updateState { copy(gender = gender, propertyDetailsError = null) }

    fun canContinuePropertyDetails(): Boolean {
        if (_uiState.value.type == null) {
            updateState {
                copy(propertyDetailsError = "Please select a property type.")
            }
            return false
        }
        if (_uiState.value.gender == null) {
            updateState {
                copy(propertyDetailsError = "Please select who can move into this property.")
            }
            return false
        }

        return true
    }

    // --- facilities ---

    fun onAddFacilities(value: String) =
        updateState {
            copy(
                facilities = facilities.toMutableSet().apply { add(value) },
                facilitiesError = null
            )
        }

    fun onRemoveFacilities(value: String) =
        updateState {
            copy(
                facilities = facilities.toMutableSet().apply { remove(value) },
                facilitiesError = null
            )
        }

    fun onAllFacilitiesRemove() =
        updateState { copy(facilities = emptySet(), facilitiesError = null) }

    fun validateFacilities(): Boolean {
        if (_uiState.value.facilities.isEmpty()) {
            updateState { copy(facilitiesError = "Please select at least one facility.") }
            return false
        }
        return true
    }

    // --- Rooms ---
    fun onAddRoom(r: Room) =
        updateState { copy(rooms = rooms.toMutableList().apply { add(r) }, roomsError = null) }

    fun onRemoveRoom(r: Room) {
        updateState { copy(rooms = rooms.toMutableList().apply { remove(r) }, roomsError = null) }
         viewModelScope.launch { fileRepo.deleteAll(r.images) }
    }

    fun onUpdate(index: Int, r: Room) = updateState {
        copy(rooms = rooms.toMutableList().apply { this[index] = r })
    }

    fun validateRooms(): Boolean {
        if (_uiState.value.rooms.isEmpty()) {
            updateState { copy(roomsError = "Please add at least one room.") }
            return false
        }
        return true
    }


    // -- navigation ----
    fun onGoNext(onSuccess: (Long) -> Unit, onError: (String) -> Unit) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) } // set state loading

            val validate = when (_uiState.value.progressSteps[currentIndex]) {
                PropertyStep.PROPERTY_DETAILS -> canContinuePropertyDetails()
                PropertyStep.BASIC_INFO -> validateBasicInfo()
                PropertyStep.LOCATION -> validateLocation()
                PropertyStep.FACILITIES -> validateFacilities()
                PropertyStep.ROOMS -> validateRooms()
            }

            if (!validate) {
                updateState { copy(isLoading = false) }
                return@launch
            }

            val curr = _uiState.value

            val nextIndex = when {
                currentIndex == (curr.progressSteps.size - 1) -> currentIndex
                else -> currentIndex + 1
            }

            if (currentIndex == curr.progressSteps.lastIndex) {

                try {
                    // TODO: Final Step: submit the from
                    val result = curr.property?.let { property ->
                        propertyRepo.update(
                            property.id,
                            PropertyUpdateRequest(
                                type = curr.type!!.toData(),
                                gender = curr.gender!!.toData(),
                                title = curr.title,
                                contactNumber = curr.contactNumber,
                                description = curr.description,
                                district = curr.district,
                                division = curr.division,
                                area = curr.area,
                                address = curr.address,
                                institutionNearby = curr.institutionNearby,
                                facilities = curr.facilities.toList()
                            )
                        )
                    } ?: propertyRepo.create(
                        PropertyRequest(
                            type = curr.type!!.toData(),
                            gender = curr.gender!!.toData(),
                            title = curr.title,
                            contactNumber = curr.contactNumber,
                            description = curr.description,
                            district = curr.district,
                            division = curr.division,
                            area = curr.area,
                            address = curr.address,
                            institutionNearby = curr.institutionNearby,
                            facilities = curr.facilities.toList(),
                            rooms = curr.rooms.map {
                                RoomRequest(
                                    availableSeats = it.availableSeats,
                                    deposit = it.deposit,
                                    images = it.images,
                                    rent = it.rent,
                                    roomHeight = it.roomHeight,
                                    roomNumber = it.roomNumber,
                                    roomWidth = it.roomWidth,
                                    type = it.type!!
                                )
                            }
                        )
                    )

                    when (result) {
                        is ApiResult.Error -> onError(result.message)
                        is ApiResult.Success -> onSuccess(result.data.id)
                    }


                } catch (_: Exception) {
                    onError("Something wat wrong! please tray again later.")
                } finally {
                    _uiState.update { it.copy(isLoading = false) }
                }

            } else {
                _uiState.update {
                    it.copy(
                        currentStep = it.progressSteps[nextIndex],
                        isNavigatingForward = true,
                        isLoading = false
                    )
                }
            }

        }
    }

    fun onGoBack() {
        if (currentIndex == 0) return

        _uiState.update {
            it.copy(
                currentStep = it.progressSteps[currentIndex - 1],
                isNavigatingForward = false
            )
        }
    }


    private val currentIndex
        get() = _uiState.value.progressSteps.indexOf(_uiState.value.currentStep)


}