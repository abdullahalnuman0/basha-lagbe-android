package dev.example.messseat.features.owner.property_add_edit

import android.net.Uri

sealed interface AsyncImageResponse {
    data class Success(
        val index: Int,
        val uri: Uri,
        val fileKey: String
    ) : AsyncImageResponse

    data class Error(
        val index: Int,
        val uri: Uri,
        val message: String
    ) : AsyncImageResponse
}