package dev.abdullah.messseat.data.dto.chat

import kotlinx.serialization.Serializable

@Serializable
data class ChatResponse(
    val id: Long = 0,
    val contextId: Long = 0,
    val otherUserId: Long = 0,
    val otherUserName: String = "",
    val otherUserAvatarUrl: String? = null,
    val lastMessagePreview: String? = null,
    val lastMessageAt: String? = null,
    val unreadCount: Int = 0,
    val createdAt: String? = null,
)