package dev.example.messseat.data.dto.notification

import dev.example.messseat.data.enums.NotyRefType
import kotlinx.serialization.Serializable

@Serializable
data class NotificationResponse(
    val id: Long = 0,
    val title: String = "",
    val body: String? = null,
    val type: String = "",
    val read: Boolean = false,
    val refType: NotyRefType? = null,
    val refId: Long? = null,
    val createdAt: String? = null,
)
