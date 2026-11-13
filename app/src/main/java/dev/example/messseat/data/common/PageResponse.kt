package dev.example.messseat.data.common

import kotlinx.serialization.Serializable

@Serializable
data class PageResponse<T>(
    val content: List<T> = emptyList(),
    val page: Int = 0,
    val size: Int = 10,
    val totalElements: Int = 0,
    val totalPages: Int = 0,
    val last: Boolean = true,
)
