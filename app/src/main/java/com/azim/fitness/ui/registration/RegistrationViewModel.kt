package com.azim.fitness.ui.registration

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.azim.fitness.data.repository.UserRepository
import com.azim.fitness.db.entity.User
import com.azim.fitness.db.entity.Weight
import com.azim.fitness.preferences.PreferencesHelper
import com.azim.fitness.utils.UIState
import kotlinx.coroutines.launch

class RegistrationViewModel(
    private val userRepository: UserRepository,
    private val preferencesHelper: PreferencesHelper
) : ViewModel() {

    private val _uiState = MutableLiveData<UIState<Long, Messages>>(UIState.Loading)
    val uiState: LiveData<UIState<Long, Messages>> = _uiState

    fun register(user: User, lifestyle: String) {
        val messages = validateUserInfo(user, lifestyle)
        if (messages.hashCode() == 0) {
            viewModelScope.launch {
                val result = userRepository.addUser(user)
                val currentWeight = Weight(
                    ownerId = user.id,
                    weight = user.weight,
                )
                userRepository.addWeight(currentWeight)
                _uiState.value = UIState.Success(result)
                preferencesHelper.isAuthorized = true
            }
        } else {
            _uiState.value = UIState.Error(messages)
        }
    }

    fun validateUserInfo(user: User, lifestyle: String): Messages {
        with(user) {
            val lastnameMessage =
                if (lastname.isBlank()) "Заполните поле!"
                else null
            val firstnameMessage =
                if (firstname.isBlank()) "Заполните поле!"
                else null
            val regex = Regex("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")
            val emailMessage =
                if (!regex.matches(email)) "Неверный формат для Email"
                else null
            val ageMessage =
                if (age !in 0..120) "Недопустимое значение возраста"
                else null
            val heightMessage =
                if (height !in 100.0f..260.0f) "Недопустимое значение роста"
                else null
            val weightMessage =
                if (weight !in 10.0f..200.0f) "Недопустимое значение веса"
                else null
            val lifestyleMessage =
                if (lifestyle.isBlank()) "Выберите хотя бы одно значение"
                else null

            val messages = Messages(
                lastname = lastnameMessage,
                firstname = firstnameMessage,
                email = emailMessage,
                age = ageMessage,
                height = heightMessage,
                weight = weightMessage,
                lifestyle = lifestyleMessage
            )
            return messages
        }
    }
}

@Suppress("UNCHECKED_CAST")
class RegistrationViewModelFactory(
    private val userRepository: UserRepository,
    private val preferencesHelper: PreferencesHelper
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RegistrationViewModel::class.java)) {
            return RegistrationViewModel(userRepository, preferencesHelper) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}