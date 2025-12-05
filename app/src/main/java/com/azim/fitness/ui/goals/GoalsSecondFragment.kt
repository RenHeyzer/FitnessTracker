package com.azim.fitness.ui.goals

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.azim.fitness.R
import com.azim.fitness.container
import com.azim.fitness.databinding.FragmentGoalsSecondBinding
import java.time.LocalDate

class GoalsSecondFragment : Fragment() {

    private var _binding: FragmentGoalsSecondBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentGoalsSecondBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        defineGoal()
        saveTargetWeight()
    }

    private fun defineGoal() {
        when (requireContext().container.preferencesHelper.goal) {
            Goal.LOOSE_WEIGHT -> binding.tvSubtitleGoal.text =
                getString(R.string.how_many_kg_lose_text)

            Goal.GAIN_MUSCLE -> binding.tvSubtitleGoal.text =
                getString(R.string.how_many_kg_gain_text)

            else -> {}
        }
    }

    private fun saveTargetWeight() {
        binding.btnContinueWeight.setOnClickListener {
            val value = binding.etTargetWeight.text.toString().trim().toFloat()
            requireContext().container.preferencesHelper.apply {
                targetWeight = value
                isGoalsDefined = true
                lastDate = LocalDate.now()
            }

            findNavController().apply {
                currentDestination?.getAction(R.id.action_goalsSecondFragment_to_mainFragment)
                    ?.let {
                        navigate(R.id.action_goalsSecondFragment_to_mainFragment)
                    }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}