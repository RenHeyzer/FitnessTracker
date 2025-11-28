package com.azim.fitness.utils

import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.ZonedDateTime

fun Instant.getDayInfoFromInstant(): Int {
    val zoneId = ZoneId.systemDefault()
    val zonedDateTime: ZonedDateTime = atZone(zoneId)
    val dayOfMonth: Int = zonedDateTime.dayOfMonth
    return dayOfMonth
}

fun Instant.instantToLocalDate(): LocalDate {
    val zoneId = ZoneId.systemDefault()
    val zonedDateTime = atZone(zoneId)
    val localDate = zonedDateTime.toLocalDate()
    return localDate
}