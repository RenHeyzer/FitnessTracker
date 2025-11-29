package com.azim.fitness.ui.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.azim.fitness.data.repository.UserRepository
import com.azim.fitness.db.entity.User
import kotlinx.coroutines.launch

class ProfileViewModel(private val userRepository: UserRepository) : ViewModel() {

    private val _userInfo = MutableLiveData<Result<User>>()
    val userInfo: LiveData<Result<User>> = _userInfo

    init {
        collectUserInfo()
    }

    private fun collectUserInfo() {
        viewModelScope.launch {
            userRepository.getUserInfo().collect { userInfo ->
                _userInfo.value = userInfo
            }
        }
    }

    fun updateLastname(newLastname: String) {
        viewModelScope.launch {
            userRepository.updateLastname(newLastname)
        }
    }
    fun updateFirstname(newFirstname: String) {
        viewModelScope.launch {
            userRepository.updateFirstname(newFirstname)
        }
    }
    fun updateEmail(newEmail: String) {
        viewModelScope.launch {
            userRepository.updateEmail(newEmail)
        }
    }
    fun updateHeight(newHeight: Float) {
        viewModelScope.launch {
            userRepository.updateHeight(newHeight)
        }
    }
    fun updatePhoto(newPhoto: String) {
        viewModelScope.launch {
            userRepository.updatePhoto(newPhoto)
        }
    }
}

@Suppress("UNCHECKED_CAST")
class ProfileViewModelFactory(
    private val userRepository: UserRepository,
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ProfileViewModel::class.java)) {
            return ProfileViewModel(
                userRepository
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}