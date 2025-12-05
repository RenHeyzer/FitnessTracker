package com.azim.fitness.ui.history

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import com.azim.fitness.R
import com.azim.fitness.databinding.DialogTodayHistoryBottomSheetBinding
import com.azim.fitness.ui.history.adapter.TodayHistoryPagerAdapter
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.tabs.TabLayoutMediator

class TodayHistoryBottomSheetDialog : BottomSheetDialogFragment() {

    private var _binding: DialogTodayHistoryBottomSheetBinding? = null
    private val binding get() = _binding!!

    private val navArgs by navArgs<TodayHistoryBottomSheetDialogArgs>()

    override fun onStart() {
        super.onStart()
        dialog?.let { d ->
            val bottomSheet = d.findViewById<View>(
                com.google.android.material.R.id.design_bottom_sheet
            )
            bottomSheet?.let { sheet ->
                val behavior = BottomSheetBehavior.from(sheet)
                behavior.state = BottomSheetBehavior.STATE_EXPANDED
                behavior.isDraggable = true
                behavior.skipCollapsed = false
                behavior.peekHeight = BottomSheetBehavior.PEEK_HEIGHT_AUTO // автоматически под высоту контента
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DialogTodayHistoryBottomSheetBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navArgs.date?.let {
            val adapter = TodayHistoryPagerAdapter(this, it)
            binding.todayHistoryViewPager.adapter = adapter
        }

        TabLayoutMediator(
            binding.todayHistoryTabLayout,
            binding.todayHistoryViewPager
        ) { tab, position ->
            tab.text = when (position) {
                0 -> getString(R.string.weight)
                1 -> getString(R.string.calories)
                2 -> getString(R.string.notes)
                else -> ""
            }
        }.attach()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding.todayHistoryViewPager.adapter = null
        _binding = null
    }
}