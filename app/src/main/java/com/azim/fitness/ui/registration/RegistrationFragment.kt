package com.azim.fitness.ui.registration

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.azim.fitness.R
import com.azim.fitness.container
import com.azim.fitness.databinding.FragmentRegistrationBinding
import com.azim.fitness.db.entity.Lifestyle
import com.azim.fitness.db.entity.User
import com.azim.fitness.utils.UIState
import kotlinx.coroutines.launch

class RegistrationFragment : Fragment() {

    private var _binding: FragmentRegistrationBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModels<RegistrationViewModel> {
        RegistrationViewModelFactory(
            userRepository = requireContext().container.userRepository,
            preferencesHelper = requireContext().container.preferencesHelper
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegistrationBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initAdapter()
        incrementAndDecrementAge()
        onRegisterButtonClick()
        observeState()
    }

    private fun initAdapter() {
        val adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_dropdown_item_1line,
            listOf(getString(R.string.sedentary_lifestyle), getString(R.string.active_lifestyle))
        )
        binding.actvLifestyle.setAdapter(adapter)
    }

    private fun incrementAndDecrementAge() = with(binding) {
        btnPlus.setOnClickListener {
            var age = etAge.text.toString().trim().toInt()
            if (age <= 120) {
                age++
                etAge.setText(age.toString())
            }
        }
        btnMinus.setOnClickListener {
            var age = etAge.text.toString().trim().toInt()
            if (age > 0) {
                age--
                etAge.setText(age.toString())
            }
        }
    }

    private fun onRegisterButtonClick() = with(binding) {
        btnRegister.setOnClickListener {
            val lastname = etSurname.text.toString().trim()
            val firstname = etFirstname.text.toString().trim()
            val age = etAge.text.toString().trim().toInt()
            val weight = etWeight.text.toString().trim().toFloat()
            val height = etHeight.text.toString().trim().toFloat()
            val email = etEmail.text.toString().trim()
            val lifestyle = actvLifestyle.text.toString()

            val user = User(
                lastname = lastname,
                firstname = firstname,
                age = age,
                weight = weight,
                height = height,
                email = email,
                lifestyle = if (lifestyle == Lifestyle.SEDENTERY.value)
                    Lifestyle.SEDENTERY else Lifestyle.ACTIIVE
            )
            viewModel.register(user, lifestyle)
        }
    }

    private fun observeState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.observe(viewLifecycleOwner) {
                    when (it) {
                        is UIState.Error<Messages> -> with(binding) {
                            tilSurname.error = it.error.lastname
                            tilFirstname.error = it.error.firstname
                            tilAge.error = it.error.age
                            tilHeight.error = it.error.height
                            tilWeight.error = it.error.weight
                            tilEmail.error = it.error.email
                            tilLifestyle.error = it.error.lifestyle
                        }

                        UIState.Loading -> {}
                        is UIState.Success<Long> -> {
                            Log.e("success", it.data.toString())
                            findNavController().navigate(R.id.action_registrationFragment_to_goalsFragment)
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