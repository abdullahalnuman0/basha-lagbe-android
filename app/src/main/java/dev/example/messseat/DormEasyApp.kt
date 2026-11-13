package dev.example.messseat

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import dev.example.messseat.core.push.ensurePushChannel

@HiltAndroidApp
class DormEasyApp : Application() {
    override fun onCreate() {
        super.onCreate()
        ensurePushChannel(this)
    }
}