package com.azim.fitness.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.azim.fitness.db.converters.LocalDateConverter
import java.time.LocalDate

enum class DayStatus {
    TODAY,
    EMPTY,
    COMPLETED,
    MISSED,
    DISABLED,
}

@Entity(tableName = "calendar_day")
data class CalendarDay(
    @PrimaryKey
    @field:TypeConverters(LocalDateConverter::class)
    val date: LocalDate,
    val status: DayStatus,
    val isCurrentMonth: Boolean
)