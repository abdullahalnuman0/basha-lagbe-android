package dev.example.messseat.features.shared.chat

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.example.messseat.core.network.ApiResult
import dev.example.messseat.core.presentation.widgets.PageLoader
import dev.example.messseat.data.common.PageResponse
import dev.example.messseat.data.dto.chat.ChatResponse
import dev.example.messseat.data.repository.ChatRepository
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val repo: ChatRepository
) : ViewModel() {

    val loader = PageLoader(viewModelScope) { page -> loadChats(page) }

    init {
        loader.loadFirst()
    }

    private suspend fun loadChats(page: Int): ApiResult<PageResponse<ChatResponse>> {
        return repo.list(page)
    }

}