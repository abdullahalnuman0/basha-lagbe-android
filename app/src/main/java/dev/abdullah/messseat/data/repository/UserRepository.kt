package dev.abdullah.messseat.data.repository

import dev.abdullah.messseat.core.data.SessionManager
import dev.abdullah.messseat.core.network.ApiResult
import dev.abdullah.messseat.core.network.safeApiCall
import dev.abdullah.messseat.data.api.UserApi
import dev.abdullah.messseat.data.dto.user.FcmTokenRequest
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