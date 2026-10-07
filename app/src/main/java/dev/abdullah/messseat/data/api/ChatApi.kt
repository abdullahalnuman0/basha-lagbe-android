package dev.abdullah.messseat.data.api

import dev.abdullah.messseat.data.common.PageResponse
import dev.abdullah.messseat.data.dto.chat.ChatResponse
import dev.abdullah.messseat.data.dto.chat.MessageResponse
import dev.abdullah.messseat.data.dto.chat.OpenChatRequest
import dev.abdullah.messseat.data.dto.chat.SendMessageRequest
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface ChatApi {

    @POST("chats/open")
    suspend fun open(@Body body: OpenChatRequest): ChatResponse

    @GET("chats")
    suspend fun list(@Query("page") page: Int, @Query("size") size: Int): PageResponse<ChatResponse>

    @GET("chats/{id}/messages")
    suspend fun messages(
        @Path("id") id: Long,
        @Query("page") page: Int,
        @Query("size") size: Int,
    ): PageResponse<MessageResponse>

    @POST("chats/{id}/messages")
    suspend fun send(@Path("id") id: Long, @Body body: SendMessageRequest): MessageResponse

    @POST("chats/{id}/read")
    suspend fun markRead(@Path("id") id: Long): MessageResponse

    @GET("chats/unread-count")
    suspend fun unreadCount(): Map<String, Long>
}