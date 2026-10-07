package dev.abdullah.messseat.core.network

import dev.abdullah.messseat.BuildConfig
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import java.util.concurrent.TimeUnit

private const val NUL = "\u0000"

class StompClient {
    private val wsUrl = BuildConfig.BASE_URL
        .replaceFirst("https://", "wss://")
        .replaceFirst("http://", "ws://")
        .substringBefore("/api/") + "/ws"

    // the realtime socket needs no read timeout so a quiet connection isn't torn down
    private val client = OkHttpClient.Builder()
        .pingInterval(20, TimeUnit.SECONDS)
        .readTimeout(0, TimeUnit.SECONDS)
        .build()

    fun subscribe(topic: String): Flow<String> = callbackFlow {
        val request = Request.Builder().url(wsUrl).build()
        var subscribed = false

        val listener = object : WebSocketListener() {
            override fun onOpen(webSocket: WebSocket, response: Response) {
                webSocket.send("CONNECT\naccept-version:1.2\nheart-beat:0,0\n\n$NUL")
            }

            override fun onMessage(webSocket: WebSocket, text: String) {
                val command = text.substringBefore('\n')
                when (command) {
                    "CONNECTED" if !subscribed -> {
                        subscribed = true
                        webSocket.send("SUBSCRIBE\nid:sub-0\ndestination:$topic\n\n$NUL")
                    }
                    "MESSAGE" -> {
                        // Body is everything after the blank line, minus the NUL terminator.
                        val body = text.substringAfter("\n\n").removeSuffix(NUL).trim()
                        if (body.isNotBlank()) trySend(body)
                    }
                    "ERROR" -> close()
                }
            }

            override fun onFailure(
                webSocket: WebSocket,
                t: Throwable,
                response: Response?
            ) {
                close()
            }

            override fun onClosed(
                webSocket: WebSocket,
                code: Int,
                reason: String
            ) {
                close()
            }
        }

        val socket = client.newWebSocket(request, listener)
        awaitClose { socket.close(1000, null) }
    }
}