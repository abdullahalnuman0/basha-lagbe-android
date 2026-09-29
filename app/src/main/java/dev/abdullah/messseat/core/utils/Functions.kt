package dev.abdullah.messseat.core.utils

import dev.abdullah.messseat.BuildConfig

fun String.toImageUrl(): String =
    "${BuildConfig.BASE_URL}files/$this"


fun Int.toMoney(): String {
    return "৳${"%,d".format(this)}"
}