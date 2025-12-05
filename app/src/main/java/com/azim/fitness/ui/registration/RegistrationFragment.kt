package com.azim.fitness.ui.registration

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
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
import com.azim.fitness.utils.loadingDialog
import kotlinx.coroutines.delay
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
        clearTilErrors()
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
            var age = etAge.text.toString().trim().toIntOrNull() ?: 0
            if (age <= 120) {
                age++
                etAge.setText(age.toString())
            }
        }
        btnMinus.setOnClickListener {
            var age = etAge.text.toString().trim().toIntOrNull() ?: 0
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
            val age = etAge.text.toString().trim().toIntOrNull() ?: 0
            val weight = etWeight.text.toString().trim().toFloatOrNull() ?: 0.0f
            val height = etHeight.text.toString().trim().toFloatOrNull() ?: 0.0f
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
        val loading = loadingDialog()
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
                            loading.dismiss()
                        }

                        UIState.Loading -> loading.show()
                        is UIState.Success<Long> -> {
                            loading.dismiss()
                            Toast.makeText(
                                requireContext(),
                                getString(R.string.register_success), Toast.LENGTH_SHORT
                            ).show()
                            viewLifecycleOwner.lifecycleScope.launch {
                                findNavController().apply {
                                    currentDestination?.getAction(R.id.action_registrationFragment_to_goalsFragment)
                                        ?.let {
                                            navigate(R.id.action_registrationFragment_to_goalsFragment)
                                        }
                                }
                            }
                        }

                        else -> {}
                    }

                }
            }
        }
    }

    private fun clearTilErrors() = with(binding) {
        etSurname.addTextChangedListener {
            if (!it.isNullOrBlank()) {
                tilSurname.isErrorEnabled = false
                tilSurname.error = null
            }
        }
        etFirstname.addTextChangedListener {
            if (!it.isNullOrBlank()) {
                tilFirstname.isErrorEnabled = false
                tilFirstname.error = null
            }
        }
        etEmail.addTextChangedListener {
            if (!it.isNullOrBlank()) {
                tilEmail.isErrorEnabled = false
                tilEmail.error = null
            }
        }
        etAge.addTextChangedListener {
            if (!it.isNullOrBlank()) {
                tilAge.isErrorEnabled = false
                tilAge.error = null
            }
        }
        etHeight.addTextChangedListener {
            if (!it.isNullOrBlank()) {
                tilHeight.isErrorEnabled = false
                tilHeight.error = null
            }
        }
        etWeight.addTextChangedListener {
            if (!it.isNullOrBlank()) {
                tilWeight.isErrorEnabled = false
                tilWeight.error = null
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}