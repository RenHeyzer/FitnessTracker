package com.azim.fitness.utils

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.ZonedDateTime

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