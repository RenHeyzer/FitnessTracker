package com.azim.fitness.ui.history.tabs.calories

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.azim.fitness.container
import com.azim.fitness.databinding.FragmentCaloriesHistoryBinding
import com.azim.fitness.ui.history.adapter.CaloriesHistoryAdapter
import com.azim.fitness.ui.history.adapter.TodayHistoryPagerAdapter
import com.azim.fitness.ui.history.tabs.weight.WeightHistoryViewModel
import com.azim.fitness.ui.history.tabs.weight.WeightHistoryViewModelFactory
import kotlin.getValue

class CaloriesHistoryFragment : Fragment() {
    private var _binding: FragmentCaloriesHistoryBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<CaloriesHistoryViewModel> {
        CaloriesHistoryViewModelFactory(
            date = arguments?.getString(TodayHistoryPagerAdapter.DATE_KEY),
            foodsRepository = requireContext().container.foodsRepository
        )
    }

    private val adapter = CaloriesHistoryAdapter()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCaloriesHistoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.rvCaloriesHistory.adapter = adapter
        processCalories()
    }

    private fun processCalories() {
        viewModel.foods.observe(viewLifecycleOwner) { foods ->
            if (foods.isEmpty()) return@observe
            adapter.submitList(foods)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding.rvCaloriesHistory.adapter = null
        _binding = null
    }
}