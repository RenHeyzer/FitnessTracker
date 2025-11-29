package com.azim.fitness.ui.goals

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.azim.fitness.R
import com.azim.fitness.container
import com.azim.fitness.databinding.FragmentGoalsBinding

class GoalsFragment : Fragment() {

    private var _binding: FragmentGoalsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentGoalsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        isButtonEnabled()
        onGoalSelect()
    }

    private fun isButtonEnabled() {
        binding.toggleGroupGoals.addOnButtonCheckedListener { _, _, isChecked ->
            if (isChecked) {
                binding.btnContinue.isEnabled = true
            }
        }
    }

    private fun onGoalSelect() {
        binding.btnContinue.setOnClickListener {
            when (binding.toggleGroupGoals.checkedButtonId) {
                R.id.btn_goal_lose_weight -> {
                    requireContext().container.preferencesHelper.goal = Goal.LOOSE_WEIGHT
                    findNavController().apply {
                        currentDestination?.getAction(R.id.action_goalsFragment_to_goalsSecondFragment)
                            ?.let {
                                navigate(R.id.action_goalsFragment_to_goalsSecondFragment)
                            }
                    }
                }

                R.id.btn_goal_gain_muscle -> {
                    requireContext().container.preferencesHelper.goal = Goal.GAIN_MUSCLE
                    findNavController().apply {
                        currentDestination?.getAction(R.id.action_goalsFragment_to_goalsSecondFragment)
                            ?.let {
                                navigate(R.id.action_goalsFragment_to_goalsSecondFragment)
                            }
                    }
                }

                R.id.btn_goal_maintain_form -> {
                    requireContext().container.preferencesHelper.apply {
                        goal = Goal.MAINTAIN_FORM
                        isGoalsDefined = true
                    }
                    findNavController().apply {
                        currentDestination?.getAction(R.id.action_goalsFragment_to_mainFragment)
                            ?.let {
                                navigate(R.id.action_goalsFragment_to_mainFragment)
                            }
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}