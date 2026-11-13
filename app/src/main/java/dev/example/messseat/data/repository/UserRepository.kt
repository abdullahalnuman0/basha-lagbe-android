package dev.example.messseat.data.repository

import dev.example.messseat.core.data.SessionManager
import dev.example.messseat.core.network.ApiResult
import dev.example.messseat.core.network.safeApiCall
import dev.example.messseat.data.api.UserApi
import dev.example.messseat.data.dto.user.FcmTokenRequest
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepository @Inject constructor(
    private val api: UserApi,
    private val session: SessionManager
) {

    suspend fun updateFcmToken(token: String): ApiResult<Unit> =
        safeApiCall { api.updateFcmToken(FcmTokenRequest(token)); Unit }

    suspend fun heartbeat() {
        runCatching { api.heartbeat() }
    }

}