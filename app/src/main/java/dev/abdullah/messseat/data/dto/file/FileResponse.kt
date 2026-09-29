package dev.abdullah.messseat.data.dto.file

import kotlinx.serialization.Serializable

@Serializable
data class FileResponse(
    val contentType: String,
    val fileKey: String,
    val size: Int
)