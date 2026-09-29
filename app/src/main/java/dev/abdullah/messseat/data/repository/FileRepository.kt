package dev.abdullah.messseat.data.repository

import android.content.Context
import android.net.Uri
import dev.abdullah.messseat.R
import dev.abdullah.messseat.core.network.ApiResult
import dev.abdullah.messseat.core.network.safeApiCall
import dev.abdullah.messseat.data.api.FileApi
import dev.abdullah.messseat.data.dto.file.FileResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import androidx.core.net.toUri
import dev.abdullah.messseat.core.utils.AppProperties


class FileRepository(
    private val api: FileApi,
    private val context: Context
) {

    suspend fun upload(uri: Uri, folder: String): ApiResult<String> = withContext(Dispatchers.IO) {
        val resolver = context.contentResolver
        val mime = resolver.getType(uri) ?: "image/jpeg"
        val bytes = runCatching {
            resolver.openInputStream(uri)?.use { it.readBytes() }
        }.getOrNull() ?: return@withContext ApiResult.Error("Couldn't read that file.")

        if (bytes.size > AppProperties.MAX_FILE_UPLOAD_BYTES)
            return@withContext ApiResult.Error("Images must be under ${AppProperties.MAX_FILE_UPLOAD_SIZE}MB.")

        val part = MultipartBody.Part.createFormData(
            name = "file",
            filename = uri.lastPathSegment?.substringAfterLast('/') ?: "upload.jpeg",
            body = bytes.toRequestBody(mime.toMediaTypeOrNull())
        )

        when (val r = safeApiCall { api.upload(part, folder) }) {
            is ApiResult.Success -> ApiResult.Success(r.data.fileKey)
            is ApiResult.Error -> r
        }
    }

    suspend fun delete(fileKey: String) = safeApiCall { api.delete(fileKey) }

    suspend fun deleteAll(fileKeys: List<String>) = safeApiCall { api.deleteAll(fileKeys) }

    val roomDefaultImage: Uri
        get() = "android.resource://${context.packageName}/${R.drawable.room_default_image}".toUri()

}