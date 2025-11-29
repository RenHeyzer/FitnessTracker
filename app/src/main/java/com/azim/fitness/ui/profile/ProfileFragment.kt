package com.azim.fitness.ui.profile

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.azim.fitness.R
import com.azim.fitness.container
import com.azim.fitness.databinding.FragmentProfileBinding
import com.bumptech.glide.Glide

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<ProfileViewModel> {
        ProfileViewModelFactory(requireContext().container.userRepository)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadUserInfo()
        onEditClick()
    }

    private fun loadUserInfo() = with(binding) {
        viewModel.userInfo.observe(viewLifecycleOwner) { result ->
            result.fold(
                onSuccess = { info ->
                    tvFullName.text = "${info.lastname} ${info.firstname}"
                    etSurname.setText(info.lastname)
                    etFirstname.setText(info.firstname)
                    etEmail.setText(info.email)
                    etHeight.setText(info.height.toString())
                    info.photo?.let {
                        Glide.with(ivProfile.context)
                            .load(it)
                            .into(ivProfile)
                    }
                },
                onFailure = {}
            )
        }
    }

    private val imagePickerLauncher = registerForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        if (uri != null) {
            try {
                val contentResolver = requireContext().contentResolver

                contentResolver.takePersistableUriPermission(
                    uri,
                    android.content.Intent.FLAG_GRANT_READ_URI_PERMISSION
                )

                viewModel.updatePhoto(uri.toString())
            } catch (e: SecurityException) {
                Log.e("SecurityError", "Не удалось получить постоянное разрешение.", e)
            }
        }
    }

    private fun onEditClick() = with(binding) {
        ivProfile.setOnClickListener {
            imagePickerLauncher.launch("image/*")
        }
        tilSurname.setEndIconOnClickListener {
            if (etSurname.isFocusable) {
                etSurname.isFocusable = false
                etSurname.isFocusableInTouchMode = false
                val newSurname = etSurname.text.toString().trim()
                viewModel.updateLastname(newSurname)
                tilSurname.endIconDrawable =
                    ContextCompat.getDrawable(requireContext(), R.drawable.ic_edit_square)
            } else {
                etSurname.isFocusable = true
                etSurname.isFocusableInTouchMode = true
                etSurname.requestFocus()
                etSurname.setSelection(etSurname.text?.length ?: 0)
                tilSurname.endIconDrawable =
                    ContextCompat.getDrawable(requireContext(), R.drawable.ic_check)
            }
        }
        tilFirstname.setEndIconOnClickListener {
            if (etFirstname.isFocusable) {
                etFirstname.isFocusable = false
                etFirstname.isFocusableInTouchMode = false
                val newFirstname = etFirstname.text.toString().trim()
                viewModel.updateFirstname(newFirstname)
                tilFirstname.endIconDrawable =
                    ContextCompat.getDrawable(requireContext(), R.drawable.ic_edit_square)
            } else {
                etFirstname.isFocusable = true
                etFirstname.isFocusableInTouchMode = true
                etFirstname.requestFocus()
                etFirstname.setSelection(etFirstname.text?.length ?: 0)
                tilFirstname.endIconDrawable =
                    ContextCompat.getDrawable(requireContext(), R.drawable.ic_check)
            }
        }
        tilEmail.setEndIconOnClickListener {
            if (etEmail.isFocusable) {
                etEmail.isFocusable = false
                etEmail.isFocusableInTouchMode = false
                val newEmail = etEmail.text.toString().trim()
                viewModel.updateEmail(newEmail)
                tilEmail.endIconDrawable =
                    ContextCompat.getDrawable(requireContext(), R.drawable.ic_edit_square)
            } else {
                etEmail.isFocusable = true
                etEmail.isFocusableInTouchMode = true
                etEmail.requestFocus()
                etEmail.setSelection(etEmail.text?.length ?: 0)
                tilEmail.endIconDrawable =
                    ContextCompat.getDrawable(requireContext(), R.drawable.ic_check)
            }
        }
        tilHeight.setEndIconOnClickListener {
            if (etHeight.isFocusable) {
                etHeight.isFocusable = false
                etHeight.isFocusableInTouchMode = false
                val newHeight = etHeight.text.toString().trim().toFloat()
                viewModel.updateHeight(newHeight)
                tilHeight.endIconDrawable =
                    ContextCompat.getDrawable(requireContext(), R.drawable.ic_edit_square)
            } else {
                etHeight.isFocusable = true
                etHeight.isFocusableInTouchMode = true
                etHeight.requestFocus()
                etHeight.setSelection(etHeight.text?.length ?: 0)
                tilHeight.endIconDrawable =
                    ContextCompat.getDrawable(requireContext(), R.drawable.ic_check)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}