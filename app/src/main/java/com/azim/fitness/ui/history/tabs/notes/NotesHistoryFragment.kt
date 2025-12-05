package com.azim.fitness.ui.history.tabs.notes

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.azim.fitness.container
import com.azim.fitness.databinding.FragmentNotesHistoryBinding
import com.azim.fitness.ui.history.adapter.NotesHistoryAdapter
import com.azim.fitness.ui.history.adapter.TodayHistoryPagerAdapter

class NotesHistoryFragment : Fragment() {
    private var _binding: FragmentNotesHistoryBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<NotesHistoryViewModel> {
        NotesHistoryViewModelFactory(
            date = arguments?.getString(TodayHistoryPagerAdapter.DATE_KEY),
            notesRepository = requireContext().container.notesRepository,
        )
    }

    private val adapter = NotesHistoryAdapter()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNotesHistoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.rvNotesHistory.adapter = adapter
        processNotes()
    }

    private fun processNotes() {
        viewModel.notes.observe(viewLifecycleOwner) { notes ->
            if (notes.isEmpty()) return@observe
            adapter.submitList(notes)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding.rvNotesHistory.adapter = null
        _binding = null
    }
}