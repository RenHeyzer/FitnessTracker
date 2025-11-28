package com.azim.fitness.data.repository

import com.azim.fitness.db.dao.DailyResultDao
import com.azim.fitness.db.entity.CalendarDay

class DailyResultRepository(private val dailyResultDao: DailyResultDao) {

    fun getDailyResults() = dailyResultDao.getDailyResults()
    suspend fun addDailyResult(day: CalendarDay) = dailyResultDao.addDailyResult(day)
}