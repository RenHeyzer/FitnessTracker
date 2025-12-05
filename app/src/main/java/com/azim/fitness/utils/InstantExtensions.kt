package com.azim.fitness.utils

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
fun Instant.getDayInfoFromInstant(): Int {
    val zoneId = ZoneId.systemDefault()
    val zonedDateTime: ZonedDateTime = atZone(zoneId)
    val dayOfMonth: Int = zonedDateTime.dayOfMonth
    return dayOfMonth
}

@RequiresApi(Build.VERSION_CODES.O)
fun Instant.toLocalDate(): LocalDate {
    return this.atOffset(ZoneOffset.UTC).toLocalDate()
}

fun Instant.toPrettyString(): String {
    val zone = ZoneId.systemDefault()
    val localDateTime = this.atZone(zone).toLocalDateTime()
    val today = LocalDate.now(zone)

    return if (localDateTime.toLocalDate() == today) {
        val time = localDateTime.format(DateTimeFormatter.ofPattern("HH:mm"))
        "Сегодня $time"
    } else {
        val date = localDateTime.format(DateTimeFormatter.ofPattern("dd.MM.yy HH:mm"))
        date
    }
}
