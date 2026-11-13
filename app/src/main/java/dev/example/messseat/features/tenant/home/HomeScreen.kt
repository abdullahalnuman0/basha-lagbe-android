package dev.example.messseat.features.tenant.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dev.example.messseat.core.presentation.components.AppSpacer
import dev.example.messseat.core.presentation.components.PullToRefresh
import dev.example.messseat.core.presentation.widgets.AppTopBar
import dev.example.messseat.core.presentation.widgets.ErrorBox
import dev.example.messseat.core.presentation.widgets.RoomItemCard
import dev.example.messseat.core.presentation.widgets.ShimmerList
import dev.example.messseat.core.presentation.widgets.UiState
import dev.example.messseat.core.utils.greeting
import dev.example.messseat.domain.model.User
import dev.example.messseat.features.tenant.home.components.AccommodateCategoriesSection
import dev.example.messseat.features.tenant.home.components.AccountDropdownOption
import dev.example.messseat.features.tenant.home.components.CampusSafeSpaceCard
import dev.example.messseat.features.tenant.home.components.NearByLabel
import dev.example.messseat.navigation.SharedRoute
import dev.example.messseat.navigation.NavRoute
import dev.example.messseat.navigation.TenantRoute
import dev.example.messseat.ui.theme.Space

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    user: User?,
    onNavigateBottom: (NavRoute) -> Unit,
    onNavigate: (NavRoute) -> Unit,
    loggedOut: () -> Unit,
    vm: HomeViewModel = hiltViewModel()
) {

    val loaderState by vm.loader.state.collectAsStateWithLifecycle()
    val refreshing by vm.loader.refreshing.collectAsStateWithLifecycle()

    Column(modifier = modifier.fillMaxSize()) {
        AppTopBar(
            title = "DormEasy", appIcon = true,
            subTitle = greeting(user?.fullName),
            actions = {
                AccountDropdownOption(
                    user = user,
                    onProfile = { onNavigate(SharedRoute.User) },
                    onSaved = { onNavigate(TenantRoute.SavedPost) },
                    onNotification = { onNavigate(SharedRoute.Notification) },
                    loggedOut = loggedOut
                )
            }
        )

        when (val s = loaderState) {
            UiState.Loading -> ShimmerList()

            is UiState.Error -> ErrorBox(
                message = s.message,
                onRetry = vm.loader::loadFirst
            )

            is UiState.Success -> PullToRefresh(refreshing, vm.loader::refresh) {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(
                        start = Space.screen,
                        end = Space.screen,
                        top = Space.md,
                        bottom = Space.listBottom
                    ),
                    verticalArrangement = Arrangement.spacedBy(Space.listGap)
                ) {

                    item {
                        CampusSafeSpaceCard { onNavigateBottom(TenantRoute.Search) }

                        AppSpacer(Space.listGap)
                        AccommodateCategoriesSection { onNavigateBottom(TenantRoute.Search) }

                        AppSpacer(Space.listGap)
                        NearByLabel { onNavigateBottom(TenantRoute.Search) }
                    }

                    items(s.data) { r ->
                        Column {
                            RoomItemCard(
                                isOwnerView = false,
                                images = r.images,
                                roomNumber = r.roomNumber,
                                propertyType = r.propertyType,
                                gender = r.gender,
                                roomType = r.roomType,
                                rent = r.rent,
                                location = "${r.area}, ${r.district}",
                                approved = true,
                                title = r.title,
                                institutionNearby = r.institutionNearby,
                                roomSize = "${r.roomWidth} x ${r.roomHeight}",
                                totalSeats = r.availableSeats,
                                onClick = { onNavigate(TenantRoute.RoomDetails(r.roomId)) }
                            )

                        }
                    }

                }

            }
        }

    }
}