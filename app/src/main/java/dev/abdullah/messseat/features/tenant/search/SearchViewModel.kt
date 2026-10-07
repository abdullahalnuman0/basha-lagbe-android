package dev.abdullah.messseat.features.tenant.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.abdullah.messseat.core.data.SessionManager
import dev.abdullah.messseat.core.network.ApiResult
import dev.abdullah.messseat.core.presentation.widgets.PageLoader
import dev.abdullah.messseat.data.common.PageResponse
import dev.abdullah.messseat.data.dto.tenant.RoomTenantResponse
import dev.abdullah.messseat.data.repository.TenantRepository
import dev.abdullah.messseat.features.auth.signup.LocationData
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val repo: TenantRepository,
    private val session: SessionManager
) : ViewModel() {

    private val _filter =
        MutableStateFlow(RoomSearchFilter())

    val filter = _filter.asStateFlow()

    private val _filterSheetVisible =
        MutableStateFlow(false)

    val filterSheetVisible =
        _filterSheetVisible.asStateFlow()

    val roomLoader =
        PageLoader<RoomTenantResponse>(scope = viewModelScope) { page -> loadRooms(page) }

    val user = session.userFlow
        .stateIn(viewModelScope, SharingStarted.Eagerly, null)

    init {
        viewModelScope.launch {
            user.filterNotNull()
                .first()
                .let { user ->
                    _filter.update { currentFilter ->
                        val division =
                            user.division ?: LocationData.divisions[0]

                        currentFilter.copy(
                            division = division,
                            district = user.district
                                ?: LocationData.districtByDivision[division]!![0]
                        )
                    }
                }

            roomLoader.loadFirst()
        }
    }

    private suspend fun loadRooms(
        page: Int
    ): ApiResult<PageResponse<RoomTenantResponse>> {

        val current = filter.value

        val params = buildMap<String, String> {

            put("page", page.toString())
            put("size", "10")

            current.keyword
                .trim()
                .takeIf { it.isNotEmpty() }
                ?.let {
                    put("keyword", it)
                }

            current.division
                .takeIf { it.isNotBlank() }
                ?.let { put("division", it) }

            current.district
                .takeIf { it.isNotBlank() }
                ?.let { put("district", it) }

            current.area
                .takeIf { it.isNotBlank() }
                ?.let { put("area", it) }

            current.roomType
                ?.let { put("roomType", it.name) }

            current.minRent
                .toIntOrNull()
                ?.let { put("minRent", it.toString()) }

            current.maxRent
                .toIntOrNull()
                ?.let { put("maxRent", it.toString()) }

            current.minAvailableSeats
                .toIntOrNull()
                ?.let { put("minAvailableSeats", it.toString()) }

            current.maxAvailableSeats
                .toIntOrNull()
                ?.let { put("maxAvailableSeats", it.toString()) }

            if (current.minRoomWidth.toIntOrNull() != null && current.minRoomHeight.toIntOrNull() != null)
                put(
                    "minRoomSize",
                    (current.minRoomWidth.toInt() * current.minRoomHeight.toInt()).toString()
                )
//            current.minRoomSize
//                .toIntOrNull()
//                ?.let { put("", it.toString()) }

            current.propertyType
                ?.let { put("propertyType", it) }

            current.gender
                ?.let { put("gender", it) }

            put(
                "sort",
                "${current.sortBy.apiValue},${current.sortOrder.apiValue}"
            )
        }

        return repo.findRooms(params)
    }

    //==============================
    // Keyword search
    //==============================
    fun updateKeyword(value: String) {
        _filter.update {
            it.copy(keyword = value)
        }
    }

    fun submitSearch() {
        roomLoader.loadFirst()
    }

    fun updateFilter(
        transform: (RoomSearchFilter) -> RoomSearchFilter
    ) {
        _filter.update(transform)
    }

    fun applyFilters() {
        _filterSheetVisible.value = false
        roomLoader.loadFirst()
    }

    fun clearFilters() {
        _filter.value = RoomSearchFilter()
        _filterSheetVisible.value = false
        roomLoader.loadFirst()
    }

    fun openFilterSheet() {
        _filterSheetVisible.value = true
    }

    fun closeFilterSheet() {
        _filterSheetVisible.value = false
    }
}