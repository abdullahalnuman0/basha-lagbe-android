package dev.example.messseat.data.api

import dev.example.messseat.data.dto.auth.AuthResponse
import dev.example.messseat.data.dto.auth.RefreshTokenRequest
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface RefreshApi {
    @POST("auth/refresh")
    fun refresh(@Body req: RefreshTokenRequest): Call<AuthResponse>
}