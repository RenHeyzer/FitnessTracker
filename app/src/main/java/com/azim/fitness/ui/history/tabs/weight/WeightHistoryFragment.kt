package com.azim.fitness.ui.history.tabs.weight

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.azim.fitness.container
import com.azim.fitness.databinding.FragmentWeightHistoryBinding
import com.azim.fitness.ui.history.adapter.TodayHistoryPagerAdapter
import com.azim.fitness.ui.history.adapter.WeightHistoryAdapter

class WeightHistoryFragment : Fragment() {

    private var _binding: FragmentWeightHistoryBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<WeightHistoryViewModel> {
        WeightHistoryViewModelFactory(
            date = arguments?.getString(TodayHistoryPagerAdapter.DATE_KEY),
            userRepository = requireContext().container.userRepository,
        )
    }

    private val adapter = WeightHistoryAdapter()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentWeightHistoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.rvWeightHistory.adapter = adapter
        processWeights()
    }

    private fun processWeights() {
        viewModel.weights.observe(viewLifecycleOwner) { weights ->
            if (weights.isEmpty()) return@observe
            adapter.submitList(weights)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding.rvWeightHistory.adapter = null
        _binding = null
    }
}