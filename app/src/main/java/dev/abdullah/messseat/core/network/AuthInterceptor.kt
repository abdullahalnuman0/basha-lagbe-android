package dev.abdullah.messseat.core.network

import dev.abdullah.messseat.core.data.SessionManager
import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor(private val session: SessionManager) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val token = session.accessToken()

        val isAuthCall = request.url.encodedPath.contains("/auth/")
        val newRequest = if (token != null && !isAuthCall)
            request.newBuilder().header("Authorization", "Bearer $token").build()
        else request

        return chain.proceed(newRequest)
    }

}