package com.azim.fitness.ui.goals

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.azim.fitness.R
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
        onGoalSelect()
    }

    private fun onGoalSelect() = with(binding) {
        toggleGroupGoals.addOnButtonCheckedListener { _, _, isChecked ->
            if (isChecked) {
                btnContinue.isEnabled = true
            }
        }
        btnContinue.setOnClickListener {
            when (toggleGroupGoals.checkedButtonId) {
                R.id.btn_goal_lose_weight ->
                    findNavController().navigate(
                        GoalsFragmentDirections.actionGoalsFragmentToGoalsSecondFragment(1)
                    )
                R.id.btn_goal_gain_muscle ->
                    findNavController().navigate(
                        GoalsFragmentDirections.actionGoalsFragmentToGoalsSecondFragment(2)
                    )
                R.id.btn_goal_gain_weight ->
                    findNavController().navigate(
                        GoalsFragmentDirections.actionGoalsFragmentToGoalsSecondFragment(3)
                    )
                R.id.btn_goal_maintain_form ->
                    findNavController().navigate(
                        GoalsFragmentDirections.actionGoalsFragmentToGoalsSecondFragment(4)
                    )
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}