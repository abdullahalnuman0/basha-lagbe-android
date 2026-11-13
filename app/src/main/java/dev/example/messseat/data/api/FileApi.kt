package dev.example.messseat.data.api

import dev.example.messseat.data.dto.file.FileResponse
import okhttp3.MultipartBody
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.HTTP
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Query

interface FileApi {

    @Multipart
    @POST("files")
    suspend fun upload(
        @Part file: MultipartBody.Part,
        @Query("folder") folder: String?
    ): FileResponse

    @DELETE("files")
    suspend fun delete(@Query("fileKey") fileKey: String)

//    @DELETE("files/batch")
//    suspend fun deleteAll(@Body body: List<String>)

    @HTTP(method = "DELETE", path = "files/batch", hasBody = true)
    suspend fun deleteAll(@Body body: List<String>)

}