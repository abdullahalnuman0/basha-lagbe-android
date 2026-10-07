package dev.abdullah.messseat.features.owner.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.SnackbarDuration
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dev.abdullah.messseat.core.presentation.components.AppSpacer
import dev.abdullah.messseat.core.presentation.components.LocalSnackbar
import dev.abdullah.messseat.core.presentation.components.PullToRefresh
import dev.abdullah.messseat.core.presentation.widgets.AppTopBar
import dev.abdullah.messseat.core.presentation.widgets.ErrorBox
import dev.abdullah.messseat.domain.model.User
import dev.abdullah.messseat.navigation.NavRoute
import dev.abdullah.messseat.navigation.OwnerRoute
import dev.abdullah.messseat.features.owner.home.components.HomeShimmer
import dev.abdullah.messseat.features.owner.home.components.DashboardInfoSection
import dev.abdullah.messseat.core.presentation.widgets.RoomItemCard
import dev.abdullah.messseat.features.tenant.home.components.AccountDropdownOption
import dev.abdullah.messseat.navigation.SharedRoute
import dev.abdullah.messseat.ui.theme.Space

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    user: User?,
    onNavigateBottom: (NavRoute) -> Unit,
    onNavigate: (NavRoute) -> Unit,
    loggedOut: () -> Unit,
    vm: HomeViewModel = hiltViewModel()
) {
    val snackbar = LocalSnackbar.current
    val state by vm.uiState.collectAsStateWithLifecycle()

    Column(modifier = modifier.fillMaxSize()) {
        AppTopBar(
            title = "DormEasy", appIcon = true,
            actions = {
                AccountDropdownOption(
                    user = user,
                    size = 40.dp,
                    onProfile = { onNavigate(SharedRoute.User) },
                    loggedOut = loggedOut
                )
            }
        )

        when {
            state.isLoading -> HomeShimmer()
            state.error != null && state.properties == null -> ErrorBox(
                message = state.error ?: "Something want wrong",
                onRetry = vm::loadData
            )

            else -> PullToRefresh(state.isRefreshing, onRefresh = {
                vm.loadData {
                    snackbar.show(
                        state.error ?: "Something want wrong!",
                        withDismissAction = true,
                        duration = SnackbarDuration.Indefinite
                    )
                }
            }) {

                LazyColumn(
                    modifier = modifier
                        .fillMaxSize()
                        .padding(
                            start = Space.md,
                            end = Space.md
                        ),
                    verticalArrangement = Arrangement.spacedBy(Space.md)
                )
                {

                    item {
                        DashboardInfoSection(
                            userName = user?.fullName,
                            summary = state.dashboardSummary,
                            sortPosition = state.sortPosition,
                            onAddListing = { onNavigate(OwnerRoute.Editor()) },
                            onSortPositionChange = vm::sortPositionChange
                        )
                    }

                    state.properties?.let { lp ->
                        val properties = when (state.sortPosition) {
                            1 -> lp.filter { it.approved }
                            2 -> lp.filter { !it.approved }
                            else -> lp
                        }
                        properties.forEach { property ->
                            items(property.rooms) { room ->
                                RoomItemCard(
                                    isOwnerView = true,
                                    images = room.images,
                                    roomNumber = room.roomNumber,
                                    propertyType = property.type,
                                    gender = property.gender,
                                    roomType = room.type,
                                    rent = room.rent,
                                    location = "${property.area}, ${property.district}",
                                    approved = property.approved,
                                    title = property.title,
                                    institutionNearby = property.institutionNearby,
                                    roomSize = "${room.roomWidth} x ${room.roomWidth}",
                                    totalSeats = room.availableSeats,
                                    totalView = room.totalViews
                                )
                            }
                        }

                    }


                    item {
                        AppSpacer(Space.listBottom)
                    }
                }

            }
        }


    }

}

