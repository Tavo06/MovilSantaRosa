package com.upsjb.movilsantarosa.core.utils

import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale

private const val DEFAULT_PATTERN = "dd/MM/yyyy"

fun Long?.toDateString(
    pattern: String = DEFAULT_PATTERN,
    locale: Locale = Locale.getDefault(),
    zoneId: ZoneId = ZoneId.systemDefault()
): String {
    if (this == null) return ""

    return try {
        val formatter = DateTimeFormatter
            .ofPattern(pattern, locale)
            .withZone(zoneId)

        formatter.format(Instant.ofEpochMilli(this))
    } catch (_: Exception) {
        ""
    }
}

fun currentTimeMillis(): Long =
    System.currentTimeMillis()

fun currentDateString(
    pattern: String = DEFAULT_PATTERN,
    locale: Locale = Locale.getDefault(),
    zoneId: ZoneId = ZoneId.systemDefault()
): String {
    return try {
        val formatter = DateTimeFormatter
            .ofPattern(pattern, locale)
            .withZone(zoneId)

        formatter.format(Instant.now())
    } catch (_: Exception) {
        ""
    }
}