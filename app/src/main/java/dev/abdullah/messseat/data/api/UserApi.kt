package dev.abdullah.messseat.data.api

import dev.abdullah.messseat.data.dto.auth.MessageResponse
import dev.abdullah.messseat.data.dto.user.FcmTokenRequest
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.PUT

interface UserApi {
    @PUT("users/me/fcm-token")
    suspend fun updateFcmToken(@Body body: FcmTokenRequest): MessageResponse

    @POST("users/me/heartbeat")
    suspend fun heartbeat(): MessageResponse
}