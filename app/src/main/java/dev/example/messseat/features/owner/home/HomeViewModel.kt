package dev.example.messseat.features.owner.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.example.messseat.core.network.ApiResult
import dev.example.messseat.data.dto.property.OwnerDashboardSummaryResponse
import dev.example.messseat.data.repository.PropertyRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val propertyRepo: PropertyRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState = _uiState.asStateFlow()
    private inline fun updateState(crossinline update: HomeUiState.() -> HomeUiState) {
        _uiState.update { it.update() }
    }


    init {
        loadData()
    }

    fun loadData(onError: (() -> Unit)? = null) {
        if (_uiState.value.isLoading || _uiState.value.isRefreshing) return

        updateState {
            copy(
                isLoading = dashboardSummary == null,
                isRefreshing = dashboardSummary != null
            )
        }

        viewModelScope.launch {
            when (val r = propertyRepo.findAll()) {
                is ApiResult.Error -> {
                    updateState {
                        copy(
                            error = r.message,
                            isLoading = false,
                            isRefreshing = false
                        )
                    }
                    onError?.let { it() }
                }

                is ApiResult.Success -> updateState {
                    println("Data: ${r.data}")
                    copy(
                        dashboardSummary = OwnerDashboardSummaryResponse(
                            activeListings = r.data.filter { it.approved }.size.toLong(),
                            pendingApproval = r.data.filter { !it.approved }.size.toLong(),
                            availableSeats = r.data.flatMap { it.rooms }
                                .sumOf { it.availableSeats }.toLong(),
                            totalRoom = r.data.flatMap { it.rooms }.size.toLong(),
                            approvedRoom = r.data.filter { it.approved }
                                .flatMap { it.rooms }.size.toLong(),
                            pendingRoom = r.data.filter { !it.approved }
                                .flatMap { it.rooms }.size.toLong(),
                        ),
                        properties = r.data,
                        isLoading = false,
                        isRefreshing = false
                    )
                }
            }
        }
    }


    fun sortPositionChange(positon: Int) = updateState { copy(sortPosition = positon) }

}