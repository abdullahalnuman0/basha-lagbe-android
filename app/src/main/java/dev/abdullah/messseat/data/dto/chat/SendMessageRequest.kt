package dev.abdullah.messseat.data.dto.chat

import dev.abdullah.messseat.data.enums.MessageType
import kotlinx.serialization.Serializable

@Serializable
data class SendMessageRequest(
    val content: String? = null,
    val type: MessageType = MessageType.TEXT,
    val attachmentUrl: String? = null,
)