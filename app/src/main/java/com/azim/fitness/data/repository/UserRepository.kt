package com.azim.fitness.data.repository

import androidx.room.Query
import com.azim.fitness.db.dao.UserDao
import com.azim.fitness.db.entity.User
import com.azim.fitness.db.entity.Weight
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map

class UserRepository(private val userDao: UserDao) {

    fun getUserInfo(): Flow<Result<User>> = userDao.getUserInfo().map {
        Result.success(it)
    }.catch {
        emit(Result.failure(it))
    }

    suspend fun getUserId() = userDao.getUserId()

    suspend fun addUser(user: User) = userDao.addUser(user)

    suspend fun updateLastname(newLastname: String) {
        userDao.updateLastname(newLastname)
    }
    suspend fun updateFirstname(newFirstname: String) {
        userDao.updateFirstname(newFirstname)
    }
    suspend fun updateEmail(newEmail: String) {
        userDao.updateEmail(newEmail)
    }
    suspend fun updateHeight(newHeight: Float) {
        userDao.updateHeight(newHeight)
    }
    suspend fun updatePhoto(newPhoto: String) {
        userDao.updatePhoto(newPhoto)
    }

    suspend fun addWeight(weight: Weight) = userDao.addWeight(weight)
    fun getCurrentWeight() = userDao.getCurrentWeight()
    suspend fun getFirstWeight() = userDao.getFirstWeight()
}