package com.azim.fitness.ui.calendar

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.azim.fitness.data.repository.DailyResultRepository
import com.azim.fitness.db.entity.CalendarDay
import com.azim.fitness.db.entity.DayStatus
import com.azim.fitness.ui.main.MainViewModel
import com.azim.fitness.utils.instantToLocalDate
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.LocalDate
import java.time.YearMonth
import java.time.temporal.WeekFields
import java.util.Locale

class CalendarViewModel(
    private val dailyResultRepository: DailyResultRepository
) : ViewModel() {

    private val _calendar = MutableLiveData(listOf<CalendarDay>())
    val calendar: LiveData<List<CalendarDay>> = _calendar

    init {
            val currentTimestamp = Instant.now()
        Log.e("timestamp", currentTimestamp.toString())
//        viewModelScope.launch {
//            val currentTimestamp = Instant.now()
//            dailyResultRepository.addDailyResult(
//                CalendarDay(
//                    date = currentTimestamp.instantToLocalDate(),
//                    status = DayStatus.COMPLETED,
//                    isCurrentMonth = true
//                )
//            )
//        }
    }

    fun generateMonthDays(yearMonth: YearMonth, events: Map<LocalDate, DayStatus>) {
        viewModelScope.launch {
            val days = mutableListOf<CalendarDay>()

            val currentDate = LocalDate.now()

            val firstDayOfMonth = yearMonth.atDay(1)

            val firstDayOfWeek = WeekFields.of(Locale.getDefault()).firstDayOfWeek
            val offset = firstDayOfMonth.dayOfWeek.value - firstDayOfWeek.value

            val startDate =
                firstDayOfMonth.minusDays(if (offset < 0) offset + 7L else offset.toLong())

            for (i in 0 until 42) {
                val date = startDate.plusDays(i.toLong())
                val isCurrentMonth = date.month == yearMonth.month

                val status = if (!isCurrentMonth) {
                    DayStatus.DISABLED
                } else {
                    if (date == currentDate) {
                        DayStatus.TODAY
                    } else {
                        events[date] ?: DayStatus.EMPTY
                    }
                }

                days.add(CalendarDay(date, status, isCurrentMonth))
            }

            val results = dailyResultRepository.getDailyResults().first()

            val replacementMap = results.associateBy { it.date }

            val finalDays = days.map { originalDay ->
                replacementMap[originalDay.date] ?: originalDay
            }

            _calendar.value = finalDays
        }
    }
}

@Suppress("UNCHECKED_CAST")
class CalendarViewModelFactory(
    private val dailyResultRepository: DailyResultRepository,
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CalendarViewModel::class.java)) {
            return CalendarViewModel(
                dailyResultRepository,
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}