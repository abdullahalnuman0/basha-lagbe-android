package dev.example.messseat.features.owner.listings

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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
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
import dev.example.messseat.core.presentation.widgets.EmptyBox
import dev.example.messseat.core.presentation.widgets.ErrorBox
import dev.example.messseat.core.presentation.widgets.ShimmerList
import dev.example.messseat.core.presentation.widgets.UiState
import dev.example.messseat.domain.model.User
import dev.example.messseat.navigation.NavRoute
import dev.example.messseat.navigation.OwnerRoute
import dev.example.messseat.features.owner.home.components.ListingItem
import dev.example.messseat.features.owner.home.components.ListingItemBtns
import dev.example.messseat.features.tenant.home.components.AccountDropdownOption
import dev.example.messseat.navigation.SharedRoute
import dev.example.messseat.ui.theme.Space

@Composable
fun ListingsScreen(
    modifier: Modifier = Modifier,
    user: User?,
    onNavigate: (NavRoute) -> Unit,
    loggedOut: () -> Unit,
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
                title = "Listings",
                subTitle = "Your listings",
                actions = {
                    AccountDropdownOption(
                        user = user,
                        onProfile = { onNavigate(SharedRoute.User) },
                        onNotification = { onNavigate(SharedRoute.Notification) },
                        loggedOut = loggedOut
                    )
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