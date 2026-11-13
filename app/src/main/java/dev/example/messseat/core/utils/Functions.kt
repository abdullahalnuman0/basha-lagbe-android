package dev.example.messseat.core.utils

import dev.example.messseat.BuildConfig

fun String.toImageUrl(): String =
    "${BuildConfig.BASE_URL}files/$this"


fun Int.toMoney(): String {
    return "৳${"%,d".format(this)}"
}

fun greeting(name: String?): String {
    val hour = java.time.LocalTime.now().hour
    val greeting = when {
        hour < 5 -> "Still up?"
        hour < 12 -> "Good morning"
        hour < 17 -> "Good afternoon"
        hour < 22 -> "Good evening"
        else -> "Wind down"
    }

    val firstName = name?.trim()?.split("\\s+".toRegex())?.firstOrNull().orEmpty()
    return if (firstName.isNotEmpty()) "$greeting, $firstName" else greeting
}