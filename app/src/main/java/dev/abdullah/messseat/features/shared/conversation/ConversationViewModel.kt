package dev.abdullah.messseat.features.shared.conversation

import android.net.Uri
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.abdullah.messseat.core.data.SessionManager
import dev.abdullah.messseat.core.network.ApiResult
import dev.abdullah.messseat.core.presentation.widgets.PageLoader
import dev.abdullah.messseat.data.common.PageResponse
import dev.abdullah.messseat.data.dto.chat.MessageResponse
import dev.abdullah.messseat.data.dto.chat.SendMessageRequest
import dev.abdullah.messseat.data.dto.tenant.RoomTenantResponse
import dev.abdullah.messseat.data.enums.MessageType
import dev.abdullah.messseat.data.repository.ChatRepository
import dev.abdullah.messseat.data.repository.FileRepository
import dev.abdullah.messseat.data.repository.TenantRepository
import dev.abdullah.messseat.navigation.SharedRoute
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ConversationViewModel @Inject constructor(
    private val repo: ChatRepository,
    private val files: FileRepository,
    private val tenant: TenantRepository,
    session: SessionManager,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val args = checkNotNull(savedStateHandle.toRoute<SharedRoute.Conversation>())
    val chatId = args.id

    val me = session.userFlow
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    private val _roomBar = MutableStateFlow<RoomTenantResponse?>(null)
    val roomBar = _roomBar.asStateFlow()

    private val _sending = MutableStateFlow(false)
    val sending = _sending.asStateFlow()

    private val _uploading = MutableStateFlow(false)
    val uploading = _uploading.asStateFlow()

    val loader = PageLoader(viewModelScope) { page -> loadChat(page) }


    init {
        loader.loadFirst()
        observeLive()
        loadRoomBar()
    }


    //==========================================
    // Public methods
    //==========================================
    fun send(text: String, onError: (String) -> Unit) {
        if (text.isBlank()) return
        _sending.value = true
        viewModelScope.launch {
            when (val r = repo.send(chatId, text.trim())) {
                is ApiResult.Success -> appendUnique(r.data)
                is ApiResult.Error -> onError(r.message)
            }
            _sending.value = false
        }
    }

    fun sendImage(uri: Uri, onError: (String) -> Unit) = viewModelScope.launch {
        _uploading.value = true
        when (val u = files.upload(uri, "chat")) {
            is ApiResult.Success -> {
                val body = SendMessageRequest(
                    type = MessageType.IMAGE,
                    attachmentUrl = u.data
                )
                when (val r = repo.send(chatId, body)) {
                    is ApiResult.Success -> appendUnique(r.data)
                    is ApiResult.Error -> onError(r.message)
                }
            }

            is ApiResult.Error -> onError(u.message)
        }
        _uploading.value = false

    }


    //==========================================
    // Private methods
    //==========================================
    private fun observeLive() = viewModelScope.launch {
        repo.liveMessage(chatId).collect { msg ->
            appendUnique(msg)
            repo.markRead(chatId)
        }
    }


    private suspend fun loadChat(page: Int): ApiResult<PageResponse<MessageResponse>> {

        return when (val result = repo.message(chatId)) {

            is ApiResult.Success -> {
                repo.markRead(chatId)

                ApiResult.Success(
                    result.data.copy(
                        content = result.data.content.reversed()
                    )
                )
            }

            is ApiResult.Error -> result
        }
    }

    private fun loadRoomBar() = viewModelScope.launch {
        val chat = (repo.list(0) as? ApiResult.Success)
            ?.data?.content?.firstOrNull { it.id == chatId } ?: return@launch

        val room = (tenant.findRoom(chat.contextId) as? ApiResult.Success)?.data
        _roomBar.update { room }
    }

    private fun appendUnique(msg: MessageResponse) {
        loader.mutate { current -> if (current.any { it.id == msg.id }) current else current + msg }
    }

}