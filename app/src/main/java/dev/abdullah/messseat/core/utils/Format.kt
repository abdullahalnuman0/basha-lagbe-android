package dev.abdullah.messseat.core.utils

import java.time.Duration
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale

private val DATE_FMT =
    DateTimeFormatter.ofPattern("d MMM yyyy", Locale.ENGLISH).withZone(ZoneId.systemDefault())
private val DATETIME_FMT =
    DateTimeFormatter.ofPattern("d MMM yyyy, h:mm a", Locale.ENGLISH)
        .withZone(ZoneId.systemDefault())


fun formatDate(iso: String?): String =
    iso?.let { runCatching { DATE_FMT.format(Instant.parse(it)) }.getOrNull() } ?: "—"

fun formatDateTime(iso: String?): String =
    iso?.let { runCatching { DATETIME_FMT.format(Instant.parse(it)) }.getOrNull() } ?: "—"

fun timeAgo(iso: String?): String {
    val instant = iso?.let { runCatching { Instant.parse(it) }.getOrNull() } ?: return "—"
    val d = Duration.between(instant, Instant.now())
    return when {
        d.toMinutes() < 1 -> "just now"
        d.toMinutes() < 60 -> "${d.toMinutes()}m"
        d.toHours() < 24 -> "${d.toHours()}h"
        d.toDays() < 7 -> "${d.toDays()}d"
        else -> formatDate(iso)
    }
}