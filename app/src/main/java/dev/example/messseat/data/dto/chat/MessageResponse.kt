package dev.example.messseat.data.dto.chat

import dev.example.messseat.data.enums.MessageType
import kotlinx.serialization.Serializable

@Serializable
data class MessageResponse(
    val id: Long = 0,
    val chatId: Long = 0,
    val senderId: Long = 0,
    val senderName: String = "",
    val content: String? = null,
    val type: MessageType = MessageType.TEXT,
    val attachmentUrl: String? = null,
    val read: Boolean = false,
    val createdAt: String? = null,
)
