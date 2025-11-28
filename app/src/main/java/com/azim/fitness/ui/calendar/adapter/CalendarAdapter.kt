package com.azim.fitness.ui.calendar.adapter

import android.graphics.drawable.GradientDrawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.azim.fitness.R
import com.azim.fitness.databinding.ItemCalendarDayBinding
import com.azim.fitness.db.entity.CalendarDay
import com.azim.fitness.db.entity.DayStatus

class CalendarAdapter() : ListAdapter<CalendarDay, CalendarAdapter.DayViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DayViewHolder {
        val binding =
            ItemCalendarDayBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        return DayViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DayViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class DayViewHolder(private val binding: ItemCalendarDayBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(day: CalendarDay) {
            binding.tvDay.text = day.date.dayOfMonth.toString()

            val context = binding.root.context
            val background = binding.tvDay.background as GradientDrawable

            val colorToday = ContextCompat.getColor(context, R.color.active_component_background)
            val colorGreen = ContextCompat.getColor(context, R.color.green)
            val colorRed = ContextCompat.getColor(context, R.color.red)
            val colorGrey = ContextCompat.getColor(context, R.color.hint_color)

            when (day.status) {
                DayStatus.TODAY -> background.setColor(colorToday)
                DayStatus.COMPLETED -> background.setColor(colorGreen)
                DayStatus.MISSED -> background.setColor(colorRed)
                DayStatus.EMPTY -> background.setColor(colorGrey)
                DayStatus.DISABLED -> background.setColor(colorGrey)
            }
        }
    }
}

private class DiffCallback : DiffUtil.ItemCallback<CalendarDay>() {
    override fun areItemsTheSame(oldItem: CalendarDay, newItem: CalendarDay) =
        oldItem.date == newItem.date

    override fun areContentsTheSame(oldItem: CalendarDay, newItem: CalendarDay) = oldItem == newItem
}