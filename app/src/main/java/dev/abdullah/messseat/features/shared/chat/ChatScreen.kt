package dev.abdullah.messseat.features.shared.chat

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChatBubbleOutline
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dev.abdullah.messseat.core.presentation.components.PullToRefresh
import dev.abdullah.messseat.core.presentation.widgets.AppTopBar
import dev.abdullah.messseat.core.presentation.widgets.EmptyBox
import dev.abdullah.messseat.core.presentation.widgets.ErrorBox
import dev.abdullah.messseat.core.presentation.widgets.ShimmerList
import dev.abdullah.messseat.core.presentation.widgets.UiState
import dev.abdullah.messseat.domain.model.User
import dev.abdullah.messseat.features.shared.chat.components.ChatItemCard
import dev.abdullah.messseat.features.tenant.home.components.AccountDropdownOption
import dev.abdullah.messseat.navigation.NavRoute
import dev.abdullah.messseat.navigation.SharedRoute
import dev.abdullah.messseat.ui.theme.Space

@Composable
fun ChatScreen(
    modifier: Modifier = Modifier,
    user: User?,
    onNavigate: (NavRoute) -> Unit,
    loggedOut: () -> Unit,
    vm: ChatViewModel = hiltViewModel()
) {
    val loaderState by vm.loader.state.collectAsStateWithLifecycle()
    val refreshing by vm.loader.refreshing.collectAsStateWithLifecycle()

    Column(modifier = modifier.fillMaxSize()) {
        AppTopBar(
            title = "Messages",
            subTitle = "Your conversations",
            actions = {
                AccountDropdownOption(
                    user = user,
                    size = 40.dp,
                    onProfile = {onNavigate(SharedRoute.User)},
                    loggedOut = loggedOut
                )
            }
        )

        // Main content
        when (val s = loaderState) {
            UiState.Loading -> ShimmerList()

            is UiState.Error -> ErrorBox(
                message = s.message,
                onRetry = vm.loader::loadFirst
            )

            is UiState.Success -> PullToRefresh(refreshing, vm.loader::refresh) {

                val chats = s.data

                if (chats.isEmpty()) {
                    EmptyBox(
                        title = "No chats yet",
                        subtitle = "A chat opens when you unlock a book or a tuition application is accepted.",
                        icon = Icons.Filled.ChatBubbleOutline,
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
                        verticalArrangement = Arrangement.spacedBy(Space.listGap)
                    ) {

                        items(chats, key = { it.id }) {
                            ChatItemCard(
                                chat = it,
                                onChatClick = { onNavigate(SharedRoute.Conversation(it.id)) })
                        }

                    }
                }

            }
        }
    }
}