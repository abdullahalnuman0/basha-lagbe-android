package dev.abdullah.messseat.features.tenant.saved_post

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DoneAll
import androidx.compose.material.icons.filled.EventSeat
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dev.abdullah.messseat.core.presentation.widgets.AppTopBar
import dev.abdullah.messseat.core.presentation.widgets.EmptyBox
import dev.abdullah.messseat.core.presentation.widgets.ErrorBox
import dev.abdullah.messseat.core.presentation.widgets.ShimmerList
import dev.abdullah.messseat.features.shared.conversation.components.RoomBarContent
import dev.abdullah.messseat.navigation.NavRoute
import dev.abdullah.messseat.navigation.TenantRoute
import dev.abdullah.messseat.ui.theme.Space

@Composable
fun SavedPostScreen(
    modifier: Modifier = Modifier,
    onNavigate: (NavRoute) -> Unit,
    onBack: () -> Unit
) {

    val vm: SavePostViewModel = hiltViewModel()
    val state by vm.uiState.collectAsStateWithLifecycle()


    Column(modifier.fillMaxSize()) {
        AppTopBar(
            title = "Saved",
            subTitle = "Your saved rooms",
            onBack = onBack
        )

        // Main context
        when {
            state.isLoading && state.rooms.isEmpty() -> ShimmerList()

            !state.isLoading && state.error != null -> ErrorBox(
                message = state.error ?: "Something want wrong, please try again.",
                onRetry = vm::loadSaveRooms
            )

            !state.isLoading && state.error == null -> if (state.rooms.isEmpty()) {
                EmptyBox(
                    title = "No room saved",
                    subtitle = "You don't save any rooms.",
                    icon = Icons.Filled.EventSeat,
                )
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(
                        start = Space.screen,
                        end = Space.screen,
                        top = Space.md,
                        bottom = Space.listBottom
                    ),
                    verticalArrangement = Arrangement.spacedBy(Space.sm)
                ) {

                    items(state.rooms, key = { it.roomId }) { r ->
                        RoomBarContent(
                            room = r,
                            onClick = { onNavigate(TenantRoute.RoomDetails(r.roomId)) })
                    }
                }
            }
        }
    }
}