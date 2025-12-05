package com.azim.fitness.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.azim.fitness.R
import com.azim.fitness.container
import com.azim.fitness.databinding.FragmentMainBinding
import com.azim.fitness.ui.goals.Goal
import com.azim.fitness.ui.main.adapter.ExercisesAdapter
import kotlinx.coroutines.launch

class MainFragment : Fragment() {

    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<MainViewModel> {
        MainViewModelFactory(
            exercisesRepository = requireContext().container.exercisesRepository,
            userRepository = requireContext().container.userRepository,
            preferencesHelper = requireContext().container.preferencesHelper,
            notesRepository = requireContext().container.notesRepository
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
        onSendNoteIconClick()
        processNoteEvents()
        processNoteState()
        clearNoteError()
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
            findNavController().apply {
                currentDestination?.getAction(R.id.action_mainFragment_to_todaysWeightDialog)?.let {
                    navigate(R.id.action_mainFragment_to_todaysWeightDialog)
                }
            }
        }
    }

    private fun processCurrentWeight() {
        setFragmentResultListener(TodaysWeightDialog.REQUEST_KEY) { _, bundle ->
            val weight = bundle.getFloat(TodaysWeightDialog.WEIGHT_KEY)
            viewModel.addWeight(weight)
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

    private fun onSendNoteIconClick() {
        binding.tilNote.setEndIconOnClickListener {
            val noteText = binding.etNote.text.toString().trim()
            viewModel.addNote(noteText)
        }
    }

    private fun processNoteEvents() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.noteEvents.collect { event ->
                when (event) {
                    NoteEvent.NoteSaved -> {
                        Toast.makeText(
                            requireContext(),
                            getString(R.string.note_saved),
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                    is NoteEvent.ShowToast -> {
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

    private fun processNoteState() {
        viewModel.noteState.observe(viewLifecycleOwner) { state ->
            state?.let {
                if (it.error == null) {
                    if (binding.etNote.text?.isNotBlank() == true) {
                        binding.etNote.text?.clear()
                    }
                } else {
                    binding.tilNote.isErrorEnabled = true
                    binding.tilNote.error = it.error
                }
            }
        }
    }

    private fun clearNoteError() {
        binding.etNote.addTextChangedListener {
            if (!it.isNullOrBlank()) {
                binding.tilNote.isErrorEnabled = false
                binding.tilNote.error = null
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding.rvExercises.adapter = null
        _binding = null
    }
}