package dev.abdullah.messseat.features.shared.conversation

import android.text.format.DateUtils.formatDateTime
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChatBubbleOutline
import androidx.compose.material.icons.outlined.Description
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.zIndex
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dev.abdullah.messseat.core.presentation.components.AppDivider
import dev.abdullah.messseat.core.presentation.components.LocalSnackbar
import dev.abdullah.messseat.core.presentation.components.PullToRefresh
import dev.abdullah.messseat.core.presentation.widgets.AppTopBar
import dev.abdullah.messseat.core.presentation.widgets.EmptyBox
import dev.abdullah.messseat.core.presentation.widgets.ErrorBox
import dev.abdullah.messseat.core.presentation.widgets.ShimmerList
import dev.abdullah.messseat.core.presentation.widgets.UiState
import dev.abdullah.messseat.core.utils.formatDateTime
import dev.abdullah.messseat.core.utils.toImageUrl
import dev.abdullah.messseat.features.shared.conversation.components.InputSection
import dev.abdullah.messseat.features.shared.conversation.components.MessageBubble
import dev.abdullah.messseat.features.shared.conversation.components.RoomBar
import dev.abdullah.messseat.ui.theme.Space

@Composable
fun ConversationScreen(
    modifier: Modifier = Modifier,
    onBack: () -> Unit
) {

    val vm: ConversationViewModel = hiltViewModel()
    val loaderState by vm.loader.state.collectAsStateWithLifecycle()
    val refreshing by vm.loader.refreshing.collectAsStateWithLifecycle()
    val sending by vm.sending.collectAsStateWithLifecycle()
    val uploading by vm.uploading.collectAsStateWithLifecycle()
    val snackbar = LocalSnackbar.current
    val listState = rememberLazyListState()

    val roomBar by vm.roomBar.collectAsStateWithLifecycle()
    val me by vm.me.collectAsStateWithLifecycle()

    val picker =
        rememberLauncherForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
            uri?.let { vm.sendImage(it) { msg -> snackbar.show(msg) } }
        }

    Column(
        modifier = modifier
            .fillMaxSize()
            .imePadding()
    ) {
        AppTopBar(
            title = "Chat",
            onBack = onBack
        )


        Box(Modifier.weight(1f)) {


            when (val s = loaderState) {
                UiState.Loading -> ShimmerList()

                is UiState.Error -> ErrorBox(
                    message = s.message,
                    onRetry = vm.loader::loadFirst
                )

                is UiState.Success -> PullToRefresh(refreshing, vm.loader::refresh) {

                    val messages = s.data

                    LaunchedEffect(messages.size) {
                        if (messages.isNotEmpty()) listState.animateScrollToItem(messages.lastIndex)
                    }
                    if (messages.isEmpty()) {
                        EmptyBox(
                            title = "No messages yet",
                            subtitle = "Say hello to start the conversation.",
                            icon = Icons.Filled.ChatBubbleOutline,
                        )
                    } else {
                        LazyColumn(
                            Modifier.fillMaxSize(),
                            state = listState,
                            contentPadding = PaddingValues(Space.screen),
                            verticalArrangement = Arrangement.spacedBy(Space.sm),
                        ) {
                            items(messages, key = { it.id }) { msg ->
                                println("File url: ${msg.attachmentUrl}")
                                println("File url: ${msg.attachmentUrl?.toImageUrl()}")
                                MessageBubble(
                                    message = msg,
                                    timestamp = formatDateTime(msg.createdAt),
                                    own = msg.senderId == me?.id,
                                )
                            }
                        }
                    }

                }
            }

            roomBar?.let { RoomBar(modifier = Modifier.zIndex(10f), room = it) }
        }

        AppDivider()

        InputSection(
            uploading = uploading,
            sending = sending,
            onMessageSend = { text -> vm.send(text) { snackbar.show(it) }; },
            onSelectImage = {
                picker.launch(
                    PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly),
                )
            }
        )

    }
}