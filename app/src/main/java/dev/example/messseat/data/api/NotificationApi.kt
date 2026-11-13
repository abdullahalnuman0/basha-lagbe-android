package dev.example.messseat.data.api

import dev.example.messseat.data.common.PageResponse
import dev.example.messseat.data.dto.auth.MessageResponse
import dev.example.messseat.data.dto.notification.NotificationResponse
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface NotificationApi {
    @GET("notifications")
    suspend fun list(@Query("page") page: Int, @Query("size") size: Int): PageResponse<NotificationResponse>

    @GET("notifications/unread-count")
    suspend fun unreadCount(): Map<String, Long>

    @POST("notifications/{id}/read")
    suspend fun markRead(@Path("id") id: Long): MessageResponse

    @POST("notifications/read-all")
    suspend fun markAllRead(): MessageResponse
}