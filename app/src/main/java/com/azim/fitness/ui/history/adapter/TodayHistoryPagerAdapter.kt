package com.azim.fitness.ui.history.adapter

import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.azim.fitness.ui.history.tabs.calories.CaloriesHistoryFragment
import com.azim.fitness.ui.history.tabs.notes.NotesHistoryFragment
import com.azim.fitness.ui.history.tabs.weight.WeightHistoryFragment

class TodayHistoryPagerAdapter(fragment: Fragment, private val date: String) :
    FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int = 3

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> WeightHistoryFragment().apply {
                arguments = bundleOf(DATE_KEY to date)
            }
            1 -> CaloriesHistoryFragment().apply {
                arguments = bundleOf(DATE_KEY to date)
            }
            2 -> NotesHistoryFragment().apply {
                arguments = bundleOf(DATE_KEY to date)
            }
            else -> Fragment()
        }
    }

    companion object {
        const val DATE_KEY = "date"
    }
}