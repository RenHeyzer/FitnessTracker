package com.azim.fitness.ui.calories

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.azim.fitness.data.repository.FoodsRepository
import com.azim.fitness.data.repository.UserRepository
import com.azim.fitness.db.entity.Food
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.LocalDate

class CaloriesViewModel(
    private val userRepository: UserRepository,
    private val foodsRepository: FoodsRepository,
) : ViewModel() {

    private val _foods = MutableLiveData(emptyList<Food>())
    val foods: LiveData<List<Food>> = _foods

    private val _foodEvents = MutableSharedFlow<FoodEvent>()
    val foodEvents = _foodEvents.asSharedFlow()

    private val _foodState = MutableLiveData(FoodUiState())
    val foodState: LiveData<FoodUiState> = _foodState

    private val _totalCalories = MutableLiveData<Float>()
    val totalCalories: LiveData<Float> = _totalCalories

    init {
        getFoods()
        getTotalCalories()
    }

    private fun getFoods() {
        viewModelScope.launch {
            val today = LocalDate.now().toString()
            foodsRepository.getTodayFoods(today).collect { foods ->
                _foods.value = foods
            }
        }
    }

    private fun getTotalCalories() {
        viewModelScope.launch {
            val today = LocalDate.now().toString()
            foodsRepository.getTotalCalories(today).collect { value ->
                _totalCalories.value = value
            }
        }
    }

    fun addFood(foodName: String, calories: String) {
        if (validateFood(foodName, calories)) return

        viewModelScope.launch {
            runCatching {
                val ownerId = userRepository.getUserId()
                val food = Food(
                    ownerId = ownerId,
                    foodName = foodName,
                    calories = calories.toFloat()
                )
                foodsRepository.addFood(food)
            }.onSuccess {
                _foodState.value = _foodState.value?.copy(
                    foodId = it,
                    error = null,
                    foodNameError = null,
                    caloriesError = null
                )
                _foodEvents.emit(FoodEvent.FoodSaved)
            }.onFailure {
                _foodState.value = _foodState.value?.copy(
                    foodId = 0L,
                    error = it.message,
                    foodNameError = null,
                    caloriesError = null
                )
                _foodEvents.emit(FoodEvent.ShowToast("Не удалось сохранить блюдо"))
            }
        }
    }

    private fun validateFood(foodName: String, calories: String): Boolean {
        val nameError = if (foodName.isBlank()) "Заполните поле" else null
        val caloriesError = if (calories.isBlank()) "Заполните поле" else null

        if (nameError != null || caloriesError != null) {
            _foodState.value = _foodState.value?.copy(
                foodNameError = nameError,
                caloriesError = caloriesError
            )
            return true
        }
        return false
    }
}

@Suppress("UNCHECKED_CAST")
class CaloriesViewModelFactory(
    private val userRepository: UserRepository,
    private val foodsRepository: FoodsRepository,
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CaloriesViewModel::class.java)) {
            return CaloriesViewModel(
                userRepository,
                foodsRepository
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

data class FoodUiState(
    val foodId: Long = 0L,
    val foodNameError: String? = null,
    val caloriesError: String? = null,
    val error: String? = null
)

sealed class FoodEvent {
    data class ShowToast(val message: String) : FoodEvent()
    object FoodSaved : FoodEvent()
}
