package dev.example.messseat.data.repository

import dev.example.messseat.core.network.ApiResult
import dev.example.messseat.core.network.StompClient
import dev.example.messseat.core.network.safeApiCall
import dev.example.messseat.data.api.ChatApi
import dev.example.messseat.data.dto.chat.ChatResponse
import dev.example.messseat.data.dto.chat.MessageResponse
import dev.example.messseat.data.dto.chat.OpenChatRequest
import dev.example.messseat.data.dto.chat.SendMessageRequest
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.serialization.json.Json
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ChatRepository @Inject constructor(
    private val api: ChatApi,
    private val stomp: StompClient,
) {

    suspend fun open(contextId: Long): ApiResult<ChatResponse> =
        safeApiCall { api.open(OpenChatRequest(contextId)) }

    suspend fun list(page: Int, size: Int = 20) = safeApiCall { api.list(page, size) }

    suspend fun message(chatId: Long) = safeApiCall { api.messages(chatId, 0, 50) }

    suspend fun send(chatId: Long, content: String): ApiResult<MessageResponse> =
        safeApiCall { api.send(chatId, SendMessageRequest(content)) }

    suspend fun send(chatId: Long, body: SendMessageRequest): ApiResult<MessageResponse> =
        safeApiCall { api.send(chatId, body) }

    suspend fun markRead(chatId: Long) =
        runCatching { api.markRead(chatId) }

    suspend fun unreadCount(): Long =
        runCatching { api.unreadCount()["unread"] ?: 0L }.getOrDefault(0L)

    fun liveMessage(chatId: Long): Flow<MessageResponse> = flow {
        stomp.subscribe("/topic/chats/$chatId").collect { json ->
            runCatching { Json.decodeFromString<MessageResponse>(json) }
                .getOrNull()?.let { emit(it) }
        }
    }
}