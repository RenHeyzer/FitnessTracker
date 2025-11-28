package com.azim.fitness.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.azim.fitness.R
import com.azim.fitness.container
import com.azim.fitness.databinding.FragmentMainBinding
import com.azim.fitness.ui.goals.Goal
import com.azim.fitness.ui.main.adapter.ExercisesAdapter

class MainFragment : Fragment() {

    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<MainViewModel> {
        MainViewModelFactory(
            exercisesRepository = requireContext().container.exercisesRepository,
            userRepository = requireContext().container.userRepository,
            dailyResultRepository = requireContext().container.dailyResultRepository,
            preferencesHelper = requireContext().container.preferencesHelper
        )
    }
    private val adapter = ExercisesAdapter(
        onExerciseCompleted = { exercise ->
            viewModel.updateExercise(exercise.id, exercise.completed)
        }
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setAdapter()
        setGoal()
        loadExercises()
        onTodayWeightClick()
        processCurrentWeight()
        calculateProgress()
    }

    private fun setAdapter() {
        binding.rvExercises.adapter = adapter
    }

    private fun setGoal() {
        with(requireContext().container.preferencesHelper) {
            when (goal) {
                Goal.LOOSE_WEIGHT -> {
                    binding.tvGoalSubtitle.text =
                        getString(R.string.your_goal, "-$targetWeight кг")
                    binding.goalProgress.max = targetWeight.toInt()
                }

                Goal.GAIN_MUSCLE -> {
                    binding.tvGoalSubtitle.text =
                        getString(R.string.your_goal, "+$targetWeight кг")
                    binding.goalProgress.max = targetWeight.toInt()
                }

                Goal.MAINTAIN_FORM -> binding.tvGoalSubtitle.text =
                    getString(R.string.your_goal, getString(R.string.keep_in_shape_button_text))
            }
        }
    }

    private fun loadExercises() {
        viewModel.exercises.observe(viewLifecycleOwner) { exercises ->
            adapter.submitList(exercises)
        }
    }

    private fun onTodayWeightClick() {
        binding.btnTodayWeight.setOnClickListener {
            findNavController().navigate(R.id.action_mainFragment_to_todaysWeightDialog)
        }
    }

    private fun processCurrentWeight() {
        findNavController().currentBackStackEntry?.savedStateHandle?.getLiveData<Float>(
            TodaysWeightDialog.WEIGHT_KEY
        )?.observe(viewLifecycleOwner) { currentWeight ->
            currentWeight?.let {
                viewModel.addWeight(it)
            }
        }
    }

    private fun calculateProgress() {
        binding.goalProgress.max = 100
        viewModel.progress.observe(viewLifecycleOwner) { progress ->
            progress?.let {
                binding.tvProgressPercent.text = "$progress%"
                binding.goalProgress.setProgress(it, true)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding.rvExercises.adapter = null
        _binding = null
    }
}