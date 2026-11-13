package dev.example.messseat.core.network

import dev.example.messseat.core.data.SessionManager
import dev.example.messseat.data.api.RefreshApi
import dev.example.messseat.data.dto.auth.RefreshTokenRequest
import kotlinx.coroutines.runBlocking
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route

class TokenAuthenticator(
    private val session: SessionManager,
    private val refreshApi: RefreshApi
) : Authenticator {

    private val lock = Any()

    override fun authenticate(route: Route?, response: Response): Request? {
        // avoid an infinite 401 loop
        if (response.request.header("Authorization-Retry") != null) return null

        synchronized(lock) {
            val current = session.accessToken()
            val send = response.request.header("Authorization")?.removePrefix("Bearer ")
            if (current != null && current != send) {
                return response.request.newBuilder()
                    .header("Authorization", "Bearer $current")
                    .header("Authorization-Retry", "1")
                    .build()
            }

            val refreshToken = session.refreshToken() ?: run {
                runBlocking { session.clear() }
                return null
            }

            val refreshed = runCatching {
                refreshApi.refresh(RefreshTokenRequest(refreshToken))
                    .execute()
            }.getOrNull()
            val body = refreshed?.body()
            if (refreshed?.isSuccessful == true && body != null && body.accessToken.isNotBlank()) {
                runBlocking { session.updateToken(body.accessToken, body.refreshToken) }
                return response.request.newBuilder()
                    .header("Authorization", "Bearer ${body.accessToken}")
                    .header("Authorization-Retry", "1")
                    .build()
            }

            runBlocking { session.clear() }
            return null
        }
    }

}