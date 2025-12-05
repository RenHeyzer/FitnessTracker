package com.azim.fitness.ui.calories

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.azim.fitness.R
import com.azim.fitness.container
import com.azim.fitness.databinding.FragmentCaloriesBinding
import com.azim.fitness.ui.calories.adapter.FoodsAdapter
import kotlinx.coroutines.launch

class CaloriesFragment : Fragment() {

    private var _binding: FragmentCaloriesBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<CaloriesViewModel> {
        CaloriesViewModelFactory(
            userRepository = requireContext().container.userRepository,
            foodsRepository = requireContext().container.foodsRepository
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCaloriesBinding.inflate(inflater, container, false)
        return binding.root
    }

    private val adapter = FoodsAdapter()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.rvFoods.adapter = adapter
        processFoods()
        onAddMealButtonClick()
        processFoodEvents()
        processFoodState()
        clearFoodError()
        processTotalCalories()
    }

    private fun processFoods() {
        viewModel.foods.observe(viewLifecycleOwner) { foods ->
            if (foods.isEmpty()) return@observe
            adapter.submitList(foods)
        }
    }

    private fun onAddMealButtonClick() {
        with(binding) {
            btnAddMeal.setOnClickListener {
                val foodName = etMealName.text.toString().trim()
                val calories = etCaloriesInput.text.toString().trim()

                viewModel.addFood(foodName, calories)
            }
        }
    }

    private fun processFoodEvents() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.foodEvents.collect { event ->
                when (event) {
                    FoodEvent.FoodSaved -> {
                        Toast.makeText(
                            requireContext(),
                            getString(R.string.meal_saved),
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                    is FoodEvent.ShowToast -> {
                        Toast.makeText(
                            requireContext(),
                            event.message,
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }
    }

    private fun processFoodState() {
        viewModel.foodState.observe(viewLifecycleOwner) { state ->
            state ?: return@observe

            if (state.foodNameError != null || state.caloriesError != null) {
                binding.tilMealName.error = state.foodNameError
                binding.tilCaloriesInput.error = state.caloriesError
                return@observe
            }

            binding.tilMealName.error = null
            binding.tilCaloriesInput.error = null

            if (binding.etMealName.text?.isNotBlank() == true)
                binding.etMealName.text?.clear()

            if (binding.etCaloriesInput.text?.isNotBlank() == true)
                binding.etCaloriesInput.text?.clear()
        }
    }

    private fun clearFoodError() {
        binding.etMealName.addTextChangedListener {
            if (!it.isNullOrBlank()) {
                binding.tilMealName.isErrorEnabled = false
                binding.tilMealName.error = null
            }
        }
        binding.etCaloriesInput.addTextChangedListener {
            if (!it.isNullOrBlank()) {
                binding.tilCaloriesInput.isErrorEnabled = false
                binding.tilCaloriesInput.error = null
            }
        }
    }

    private fun processTotalCalories() {
        viewModel.totalCalories.observe(viewLifecycleOwner) {
            it ?: return@observe
            binding.tvTotalCaloriesValue.text = "%.2f".format(it)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding.rvFoods.adapter = null
        _binding = null
    }
}