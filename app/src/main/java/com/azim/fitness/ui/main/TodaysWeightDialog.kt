package com.azim.fitness.ui.main

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.navigation.fragment.findNavController
import com.azim.fitness.R
import com.azim.fitness.databinding.DialogTodaysWeightBinding

class TodaysWeightDialog : DialogFragment() {

    private var _binding: DialogTodaysWeightBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        _binding = DialogTodaysWeightBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnSaveWeight.setOnClickListener {
            val currentWeight = binding.etCurrentWeight.text.toString().trim()
            if (currentWeight.isNotBlank() && currentWeight != "0.0" && currentWeight != "0") {
                findNavController().apply {
                    previousBackStackEntry?.savedStateHandle?.set(
                        WEIGHT_KEY,
                        currentWeight.toFloat()
                    )
                    popBackStack()
                }
            } else {
                binding.tilCurrentWeight.error = getString(R.string.input_current_weight)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        const val WEIGHT_KEY = "weight"
    }
}