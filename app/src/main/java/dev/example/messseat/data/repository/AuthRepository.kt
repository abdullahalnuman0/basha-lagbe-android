package dev.example.messseat.data.repository

import dev.example.messseat.core.data.SessionManager
import dev.example.messseat.core.network.ApiResult
import dev.example.messseat.core.network.safeApiCall
import dev.example.messseat.data.api.AuthApi
import dev.example.messseat.data.dto.auth.AuthResponse
import dev.example.messseat.data.dto.auth.EmailRequest
import dev.example.messseat.data.dto.auth.LoginRequest
import dev.example.messseat.data.dto.auth.MessageResponse
import dev.example.messseat.data.dto.auth.OtpVerifyRequest
import dev.example.messseat.data.dto.auth.PasswordResetRequest
import dev.example.messseat.data.dto.auth.RefreshTokenRequest
import dev.example.messseat.data.dto.auth.RegisterRequest
import dev.example.messseat.data.dto.user.UserResponse

class AuthRepository(
    private val api: AuthApi,
    private val session: SessionManager
) {
    suspend fun verifyEmail(body: EmailRequest): ApiResult<Unit> = safeApiCall {
        api.verifyEmail(body)
    }

    suspend fun register(req: RegisterRequest): ApiResult<UserResponse> = safeApiCall {
        println("Error -> Api Calling")
        api.register(req)
    }

    suspend fun verifyOtp(email: String, code: String): ApiResult<AuthResponse> = safeApiCall {
        api.verifyOtp(OtpVerifyRequest(email, code)).also { session.saveSession(it) }
    }

    suspend fun resendOtp(email: String): ApiResult<MessageResponse> = safeApiCall {
        api.resendOtp(EmailRequest(email))
    }

    suspend fun login(email: String, password: String): ApiResult<AuthResponse> = safeApiCall {
        api.login(LoginRequest(email, password)).also { session.saveSession(it) }
    }

    suspend fun forgotPassword(email: String) = safeApiCall {
        api.forgotPassword(EmailRequest(email))
    }

    suspend fun resetPassword(email: String, code: String, newPdw: String) = safeApiCall {
        api.resetPassword(PasswordResetRequest(email, code, newPdw))
    }

    suspend fun logout() {
        runCatching {
            session.refreshToken()?.let {
                api.logout(RefreshTokenRequest(it))
            }
        }
        session.clear()
    }

}