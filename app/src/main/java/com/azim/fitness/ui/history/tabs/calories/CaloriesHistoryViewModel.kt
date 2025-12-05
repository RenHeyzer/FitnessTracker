package com.azim.fitness.ui.history.tabs.calories

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.azim.fitness.data.repository.FoodsRepository
import com.azim.fitness.db.entity.Food
import kotlinx.coroutines.launch

class CaloriesHistoryViewModel(
    private val date: String? = null,
    private val foodsRepository: FoodsRepository
) : ViewModel() {

    private val _foods = MutableLiveData(emptyList<Food>())
    val foods: LiveData<List<Food>> = _foods

    init {
        getTodayCalories()
    }

    private fun getTodayCalories() {
        if (date == null) return
        viewModelScope.launch {
            foodsRepository.getTodayFoods(date).collect { foods ->
                _foods.value = foods
            }
        }
    }
}

@Suppress("UNCHECKED_CAST")
class CaloriesHistoryViewModelFactory(
    private val date: String? = null,
    private val foodsRepository: FoodsRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CaloriesHistoryViewModel::class.java)) {
            return CaloriesHistoryViewModel(
                date,
                foodsRepository
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}