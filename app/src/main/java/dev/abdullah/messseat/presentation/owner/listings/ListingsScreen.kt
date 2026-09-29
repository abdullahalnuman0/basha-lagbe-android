package dev.abdullah.messseat.presentation.owner.listings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.NotificationsNone
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dev.abdullah.messseat.core.presentation.components.OnBottomReached
import dev.abdullah.messseat.core.presentation.components.PullToRefresh
import dev.abdullah.messseat.core.presentation.widgets.AppTopBar
import dev.abdullah.messseat.core.presentation.widgets.EmptyBox
import dev.abdullah.messseat.core.presentation.widgets.ErrorBox
import dev.abdullah.messseat.core.presentation.widgets.ShimmerList
import dev.abdullah.messseat.core.presentation.widgets.UiState
import dev.abdullah.messseat.navigation.NavRoute
import dev.abdullah.messseat.navigation.OwnerRoute
import dev.abdullah.messseat.presentation.owner.home.components.ListingItem
import dev.abdullah.messseat.presentation.owner.home.components.ListingItemBtns
import dev.abdullah.messseat.ui.theme.Space

@Composable
fun ListingsScreen(
    modifier: Modifier = Modifier,
    onNavigate: (NavRoute) -> Unit,
    vm: ListingsViewModel = hiltViewModel()
) {

    val state by vm.properties.state.collectAsStateWithLifecycle()
    val refreshing by vm.properties.refreshing.collectAsStateWithLifecycle()
    val appending by vm.properties.appending.collectAsStateWithLifecycle()
    val lazyListState = rememberLazyListState()

    lazyListState.OnBottomReached { vm.properties.loadMore() }

    Box(modifier.fillMaxSize()) {
        Column(modifier = modifier.fillMaxSize()) {
            AppTopBar(
                title = "My Listings", appIcon = true,
                actions = {
                    Row {
                        IconButton(onClick = {}) {
                            Icon(Icons.Filled.Logout, "Logout")
                        }
                        IconButton(onClick = { }) {
                            Icon(Icons.Filled.NotificationsNone, "Notification")
                        }
                    }
                }
            )

            when (val s = state) {
                UiState.Loading -> ShimmerList()

                is UiState.Error -> ErrorBox(
                    message = s.message,
                    onRetry = vm.properties::loadFirst
                )

                is UiState.Success -> PullToRefresh(refreshing, vm.properties::refresh) {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        state = lazyListState,
                        contentPadding = PaddingValues(
                            start = Space.screen,
                            end = Space.screen,
                            top = Space.md,
                            bottom = Space.listBottom
                        ),
                        verticalArrangement = Arrangement.spacedBy(Space.listGap)
                    ) {

                        if (s.data.isEmpty()) {
                            item {
                                EmptyBox(
                                    title = "Nothing nearby yet",
                                    subtitle = "New Property posts listings will appear here.",
                                    icon = Icons.Filled.Search,
                                )
                            }
                        } else {
                            items(s.data) { property ->
                                ListingItem(
                                    property = property,
                                    moreContent = {
                                        ListingItemBtns(
                                            onEdit = {onNavigate(OwnerRoute.Editor(property))},
                                            onView = {onNavigate(OwnerRoute.Details(property.id))}
                                        )
                                    }
                                )
                            }
                        }

                        if (appending) {
                            item {
                                Box(
                                    Modifier
                                        .fillMaxWidth()
                                        .padding(Space.lg),
                                    contentAlignment = Alignment.Center,
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

        // --- Add listing button ---
        ExtendedFloatingActionButton(
            text = { Text("Add Listing") },
            icon = { Icon(Icons.Outlined.Add, "Add") },
            onClick = { onNavigate(OwnerRoute.Editor()) },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(Space.screen)
        )
    }
}