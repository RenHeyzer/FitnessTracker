package com.azim.fitness.data.repository

import com.azim.fitness.db.dao.ProfileDao
import com.azim.fitness.db.entity.UserEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map

class UserRepository(private val profileDao: ProfileDao) {
    fun getUserInfo(): Flow<Result<UserEntity>> = profileDao.getUserInfo().map {
        Result.success(it)
    }.catch {
        emit(Result.failure(it))
    }

    suspend fun addUser(user: UserEntity) = profileDao.addUser(user)
}