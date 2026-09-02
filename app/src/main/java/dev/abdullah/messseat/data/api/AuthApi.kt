package dev.abdullah.messseat.data.api

import dev.abdullah.messseat.data.dto.auth.AuthResponse
import dev.abdullah.messseat.data.dto.auth.EmailRequest
import dev.abdullah.messseat.data.dto.auth.LoginRequest
import dev.abdullah.messseat.data.dto.auth.MessageResponse
import dev.abdullah.messseat.data.dto.auth.PasswordResetRequest
import dev.abdullah.messseat.data.dto.auth.RefreshTokenRequest
import dev.abdullah.messseat.data.dto.auth.RegisterRequest
import dev.abdullah.messseat.data.dto.auth.OtpVerifyRequest
import dev.abdullah.messseat.data.dto.user.UserResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApi {

    @POST("auth/email-verify")
    suspend fun verifyEmail(@Body body: EmailRequest)

    @POST("auth/register")
    suspend fun register(@Body body: RegisterRequest): UserResponse

    @POST("auth/verify-otp")
    suspend fun verifyOtp(@Body body: OtpVerifyRequest): AuthResponse

    @POST("auth/resend-otp")
    suspend fun resendOtp(@Body body: EmailRequest): MessageResponse

    @POST("auth/login")
    suspend fun login(@Body body: LoginRequest): AuthResponse

//    @POST("auth/recover")
//    suspend fun recover(@Body body: VerifyOtpRequest): AuthResponse

//    @POST("auth/recover/resend")
//    suspend fun resendRecovery(@Body body: EmailRequest): MessageResponse

    @POST("auth/forgot-password")
    suspend fun forgotPassword(@Body body: EmailRequest): MessageResponse

    @POST("auth/reset-password")
    suspend fun resetPassword(@Body body: PasswordResetRequest): MessageResponse

    @POST("auth/logout")
    suspend fun logout(@Body body: RefreshTokenRequest): MessageResponse
}