package com.azim.fitness.data.repository

import com.azim.fitness.db.dao.FoodsDao
import com.azim.fitness.db.entity.Food

class FoodsRepository(private val foodsDao: FoodsDao) {

    fun getTodayFoods(date: String) = foodsDao.getTodayFoods(date)
    fun getTotalCalories(date: String) = foodsDao.getTotalCalories(date)

    suspend fun addFood(food: Food) = foodsDao.addFood(food)
}