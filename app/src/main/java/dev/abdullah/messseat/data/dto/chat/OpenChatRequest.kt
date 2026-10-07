package dev.abdullah.messseat.data.dto.chat

import kotlinx.serialization.Serializable

@Serializable
data class OpenChatRequest(val contextId: Long)
