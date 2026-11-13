package dev.example.messseat.data.repository

import dev.example.messseat.core.network.StompClient
import dev.example.messseat.core.network.safeApiCall
import dev.example.messseat.data.api.NotificationApi
import dev.example.messseat.data.dto.notification.NotificationResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.serialization.json.Json
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NotificationRepository @Inject constructor(
    private val api: NotificationApi,
    private val stomp: StompClient,
) {
    suspend fun notifications(page: Int) = safeApiCall { api.list(page, 20) }

    suspend fun notificationUnread(): Long =
        runCatching { api.unreadCount()["unread"] ?: 0L }.getOrDefault(0L)

    suspend fun markNotificationRead(id: Long) {
        runCatching { api.markRead(id) }
    }

    suspend fun markAllNotificationsRead() = safeApiCall { api.markAllRead(); Unit }

    fun liveMessage(userId: Long): Flow<NotificationResponse> = flow {
        stomp.subscribe("/topic/users/$userId/notifications").collect { json ->
            runCatching { Json.decodeFromString<NotificationResponse>(json) }
                .getOrNull()?.let { emit(it) }
        }
    }

}