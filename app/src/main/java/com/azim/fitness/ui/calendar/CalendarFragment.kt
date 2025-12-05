package com.azim.fitness.ui.calendar

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.azim.fitness.container
import com.azim.fitness.databinding.FragmentCalendarBinding
import com.azim.fitness.db.entity.DayStatus
import com.azim.fitness.ui.calendar.adapter.CalendarAdapter
import java.time.LocalDate
import java.time.YearMonth

class CalendarFragment : Fragment() {

    private var _binding: FragmentCalendarBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<CalendarViewModel> {
        CalendarViewModelFactory(requireContext().container.dailyResultRepository)
    }

    private val adapter = CalendarAdapter()

    private var currentMonth = YearMonth.now()
    private val eventsMap = mutableMapOf<LocalDate, DayStatus>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCalendarBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.recyclerViewCalendar.adapter = adapter

        updateCalendar()

        binding.ivNextMonth.setOnClickListener {
            currentMonth = currentMonth.plusMonths(1)
            updateCalendar()
        }
        binding.ivPreviousMonth.setOnClickListener {
            currentMonth = currentMonth.minusMonths(1)
            updateCalendar()
        }
    }

    private fun updateCalendar() {
        binding.tvMonth.text = "${currentMonth.month.name} ${currentMonth.year}"

        viewModel.generateMonthDays(currentMonth, eventsMap)
        viewModel.calendar.observe(viewLifecycleOwner) { days ->
            adapter.submitList(days)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding.recyclerViewCalendar.adapter = null
        _binding = null
    }
}