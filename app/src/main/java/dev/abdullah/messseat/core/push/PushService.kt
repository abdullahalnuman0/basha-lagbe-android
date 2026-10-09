package dev.abdullah.messseat.core.push

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import dagger.hilt.android.AndroidEntryPoint
import dev.abdullah.messseat.MainActivity
import dev.abdullah.messseat.R
import dev.abdullah.messseat.core.data.SessionManager
import dev.abdullah.messseat.core.network.ApiResult
import dev.abdullah.messseat.data.repository.UserRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import retrofit2.http.Body
import javax.inject.Inject

const val PUSH_CHANNEL_ID = "dorameasy_default"

private const val PUSH_TAG = "DoramEasyPush"

fun registerPushToken(
    repo: UserRepository,
    scope: CoroutineScope
) {
    runCatching {
        FirebaseMessaging.getInstance().token
            .addOnSuccessListener { token ->
                Log.i(PUSH_TAG, "FCM token acquired; registering with the backend")
                scope.launch {
                    when (val r = repo.updateFcmToken(token)) {
                        is ApiResult.Success ->
                            Log.i(PUSH_TAG, "FCM token registered.")

                        is ApiResult.Error ->
                            Log.w(
                                PUSH_TAG,
                                "Backend rejected the FCM token.\n->Error: ${r.message}"
                            )
                    }
                }
            }
            .addOnFailureListener { e ->
                Log.w(
                    PUSH_TAG,
                    "Could not obtain an FCM token: ${e.message}"
                )
            }
    }.onFailure {
        Log.w(
            PUSH_TAG,
            "Firebase is not configured - add app/googel-services.json to enable push. (${it.message}"
        )
    }
}

fun ensurePushChannel(context: Context) {
    val channel = NotificationChannel(
        PUSH_CHANNEL_ID,
        "General notifications",
        NotificationManager.IMPORTANCE_DEFAULT
    ).apply { description = "Applications, messages, posts updates" }
    context.getSystemService(NotificationManager::class.java)
        ?.createNotificationChannel(channel)
}

@AndroidEntryPoint
class PushService : FirebaseMessagingService() {
    @Inject
    lateinit var userRepo: UserRepository

    @Inject
    lateinit var session: SessionManager

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    override fun onNewToken(token: String) {
        if (session.accessToken() == null) return
        scope.launch { runCatching { userRepo.updateFcmToken(token) } }
    }

    override fun onMessageReceived(message: RemoteMessage) {
        val title = message.notification?.title ?: message.data["title"] ?: "DoramEasy"
        val body = message.notification?.body ?: message.data["body"].orEmpty()
    }


    @SuppressLint("MissingPermission")
    private fun showNotification(title: String, body: String) {
        ensurePushChannel(this)
        val intent = Intent(this, MainActivity::class.java)
            .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)
        val pending = PendingIntent.getActivity(
            this, 0, intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val notification = NotificationCompat.Builder(this, PUSH_CHANNEL_ID)
            .setSmallIcon(R.mipmap.ic_launcher_round)
            .setContentTitle(title)
            .setContentText(body)
            .setStyle(NotificationCompat.BigTextStyle().bigText(body))
            .setAutoCancel(true)
            .setContentIntent(pending)
            .build()
        runCatching {
            NotificationManagerCompat.from(this)
                .notify(System.currentTimeMillis().toInt(), notification)
        }
    }
}