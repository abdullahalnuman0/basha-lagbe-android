package dev.abdullah.messseat.features.shared.notification

import android.Manifest
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.media.AudioManager
import android.media.ToneGenerator
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import android.provider.Settings
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat

object NotificationHelper {
    fun catPostNotifications(context: Context): Boolean {
        val granted = Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU ||
                ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.POST_NOTIFICATIONS
                ) == PackageManager.PERMISSION_GRANTED
        return granted && NotificationManagerCompat.from(context).areNotificationsEnabled()
    }

    fun openNotificationSettings(context: Context) {
        launch(
            context,
            Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS).putExtra(
                Settings.EXTRA_APP_PACKAGE,
                context.packageName
            )
        )
    }

    fun inAppNotyAlert(
        context: Context,
        isSoundEnable: Boolean,
        isVibrationEnable: Boolean
    ) {
        if (isSoundEnable) playSound(context)
        if (isVibrationEnable) startVibration(context)
    }

    private fun playSound(context: Context) {
        runCatching {
            val toneGenerator = ToneGenerator(
                AudioManager.STREAM_NOTIFICATION,
                80
            )

            toneGenerator.startTone(
                ToneGenerator.TONE_PROP_BEEP,
                150
            )

            Handler(Looper.getMainLooper()).postDelayed(
                { toneGenerator.release() },
                200
            )
        }
    }

    @Suppress("DEPRECATION")
    private fun startVibration(context: Context) {
        // Vibration
        runCatching {
            val vibrator = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                context.getSystemService(VibratorManager::class.java)
                    .defaultVibrator
            } else {
                context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
            }

            if (vibrator.hasVibrator()) {
                vibrator.vibrate(
                    VibrationEffect.createOneShot(
                        150,
                        VibrationEffect.DEFAULT_AMPLITUDE
                    )
                )
            }
        }
    }


    private fun launch(context: Context, intent: Intent) {
        try {
            context.startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK))
        } catch (_: ActivityNotFoundException) {
            context.startActivity(Intent(Settings.ACTION_SETTINGS).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK))
        }
    }
}