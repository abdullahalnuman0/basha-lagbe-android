package dev.example.messseat.features.tenant.search

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dev.example.messseat.core.presentation.components.OnBottomReached
import dev.example.messseat.core.presentation.components.PullToRefresh
import dev.example.messseat.core.presentation.widgets.AppTopBar
import dev.example.messseat.core.presentation.widgets.ErrorBox
import dev.example.messseat.core.presentation.widgets.RoomItemCard
import dev.example.messseat.core.presentation.widgets.ShimmerList
import dev.example.messseat.core.presentation.widgets.UiState
import dev.example.messseat.features.tenant.search.components.RoomFilterBottomSheet
import dev.example.messseat.features.tenant.search.components.SearchHeader
import dev.example.messseat.navigation.NavRoute
import dev.example.messseat.navigation.TenantRoute
import dev.example.messseat.ui.theme.Space

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    modifier: Modifier = Modifier,
    onNavigate: (NavRoute) -> Unit,
    vm: SearchViewModel = hiltViewModel()
) {

    val loaderState by vm.roomLoader.state.collectAsStateWithLifecycle()

    val refreshing by
    vm.roomLoader.refreshing.collectAsStateWithLifecycle()

    val appending by
    vm.roomLoader.appending.collectAsStateWithLifecycle()

    val filter by
    vm.filter.collectAsStateWithLifecycle()

    val sheetVisible by
    vm.filterSheetVisible.collectAsStateWithLifecycle()

    val lazyListState =
        rememberLazyListState()

    val sheetState =
        rememberModalBottomSheetState(
            skipPartiallyExpanded = true
        )

    lazyListState.OnBottomReached {
        vm.roomLoader.loadMore()
    }

    Column(
        modifier = modifier.fillMaxSize()
    ) {

        AppTopBar(
            title = "Find a Room",
            appIcon = false,
            content = {
                SearchHeader(
                    keyword = filter.keyword,
                    filter = filter,
                    onKeywordChange = vm::updateKeyword,
                    onSearch = vm::submitSearch,
                    onFilterClick = vm::openFilterSheet
                )
            }
        )



        when (val state = loaderState) {

            UiState.Loading -> {
                ShimmerList()
            }

            is UiState.Error -> {
                ErrorBox(
                    message = state.message,
                    onRetry = vm.roomLoader::loadFirst
                )
            }

            is UiState.Success -> {

                if (state.data.isEmpty()) {

                    EmptySearchResult()

                } else {

                    PullToRefresh(
                        refreshing = refreshing,
                        onRefresh = vm.roomLoader::refresh
                    ) {

                        LazyColumn(
                            modifier = Modifier.fillMaxSize(),
                            state = lazyListState,
                            contentPadding = PaddingValues(
                                start = Space.screen,
                                end = Space.screen,
                                top = Space.md,
                                bottom = Space.listBottom
                            ),
                            verticalArrangement =
                                Arrangement.spacedBy(Space.xl)
                        ) {

                            items(
                                items = state.data,
                                key = { room ->
                                    room.roomId
                                }
                            ) { room ->

                                RoomItemCard(
                                    isOwnerView = false,
                                    images = room.images,
                                    roomNumber = room.roomNumber,
                                    propertyType = room.propertyType,
                                    gender = room.gender,
                                    roomType = room.roomType,
                                    rent = room.rent,
                                    location = "${room.area}, ${room.district}",
                                    approved = true,
                                    title = room.title,
                                    institutionNearby = room.institutionNearby,
                                    roomSize = "${room.roomWidth} x ${room.roomHeight}",
                                    totalSeats = room.availableSeats,
                                    onClick = { onNavigate(TenantRoute.RoomDetails(room.roomId)) }
                                )
                            }

                            if (appending) {

                                item {

                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(
                                                vertical = Space.lg
                                            ),
                                        contentAlignment =
                                            Alignment.Center
                                    ) {

                                        CircularProgressIndicator(
                                            strokeWidth = 2.dp,
                                            modifier = Modifier.size(24.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    if (sheetVisible) {

        RoomFilterBottomSheet(
            filter = filter,
            onFilterChange = vm::updateFilter,
            onApply = vm::applyFilters,
            onClear = vm::clearFilters,
            onDismiss = vm::closeFilterSheet,
            sheetState = sheetState
        )
    }
}


@Composable
private fun EmptySearchResult() {

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {

        Column(
            horizontalAlignment =
                Alignment.CenterHorizontally
        ) {

            Text(
                text = "No rooms found"
            )

            Text(
                text =
                    "Try changing your filters or location."
            )
        }
    }
}

