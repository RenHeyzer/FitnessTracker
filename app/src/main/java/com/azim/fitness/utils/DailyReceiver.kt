package com.azim.fitness.utils

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.azim.fitness.container
import com.azim.fitness.db.entity.CalendarDay
import com.azim.fitness.db.entity.DayStatus
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.time.Instant

class DailyReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        context?.let {
            CoroutineScope(Dispatchers.IO).launch {
                with(it.container) {
                    val currentTimestamp = Instant.now()
                    val weight = userDao.getCurrentWeight().first()
                    val wasTodayWeighing =
                        weight.timestamp.getDayInfoFromInstant() == currentTimestamp.getDayInfoFromInstant()


                    val exercises = exercisesDao.getLocalExercises().first()
                    val status =
                        if (exercises.all { exercise -> exercise.completed } && wasTodayWeighing) {
                            DayStatus.COMPLETED
                        } else {
                            DayStatus.MISSED
                        }

                    dailyResultDao.addDailyResult(
                        CalendarDay(
                            date = currentTimestamp.instantToLocalDate(),
                            status = status,
                            isCurrentMonth = true
                        )
                    )
                    exercisesDao.clearAll()
                }
                DailyAlarmScheduler.scheduleDailyReset(it)
            }
        }
    }
}