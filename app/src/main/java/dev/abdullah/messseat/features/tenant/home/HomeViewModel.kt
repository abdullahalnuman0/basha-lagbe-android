package dev.abdullah.messseat.features.tenant.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.abdullah.messseat.core.data.SessionManager
import dev.abdullah.messseat.core.network.ApiResult
import dev.abdullah.messseat.core.presentation.widgets.PageLoader
import dev.abdullah.messseat.core.utils.AppProperties
import dev.abdullah.messseat.data.common.PageResponse
import dev.abdullah.messseat.data.dto.tenant.RoomTenantResponse
import dev.abdullah.messseat.data.repository.TenantRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repo: TenantRepository,
    private val session: SessionManager
) : ViewModel() {

    val loader = PageLoader(viewModelScope) { page ->
        loadNearbyRooms(page)
    }

    val user = session.userFlow
        .stateIn(
            viewModelScope,
            SharingStarted.Eagerly,
            null
        )

    init {
        viewModelScope.launch {
            val currentUser = user
                .filterNotNull()
                .first()

            // User loaded → এখনই first page load
            loader.loadFirst()
        }
    }

    private suspend fun loadNearbyRooms(
        page: Int
    ): ApiResult<PageResponse<RoomTenantResponse>> {

        val currentUser = user.value
            ?: error("User must be loaded before loading nearby rooms")

        val params = mapOf(
            "page" to page.toString(),
            "size" to AppProperties.FEED_PAGE_SIZE.toString(),
            "division" to currentUser.division,
            "district" to currentUser.district
        )

        return repo.findRooms(params)
    }
}