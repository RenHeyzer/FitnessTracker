package com.azim.fitness.ui.main.adapter

import android.graphics.Paint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.azim.fitness.db.entity.Exercise
import com.azim.fitness.databinding.ItemExerciseBinding

class ExercisesAdapter(
    private val onExerciseCompleted: (exercise: Exercise) -> Unit,
) :
    ListAdapter<Exercise, ExercisesAdapter.ExerciseViewHolder>(ExercisesDiffUtil()) {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ExerciseViewHolder = ExerciseViewHolder(
        ItemExerciseBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    override fun onBindViewHolder(
        holder: ExerciseViewHolder,
        position: Int
    ) {
        with(holder.binding) {
            val exercise = getItem(position)
            cbTask.text = if (exercise.sets != 0) {
                "${exercise.name} — ${exercise.sets} подхода по ${exercise.reps}"
            } else {
                "${exercise.name} — ${exercise.reps}"
            }
            cbTask.isChecked = exercise.completed

            if (exercise.completed) {
                cbTask.paintFlags = cbTask.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
            } else {
                cbTask.paintFlags = cbTask.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
            }

            cbTask.setOnCheckedChangeListener { checkbox, checked ->
                exercise.completed = checked
                if (exercise.completed) {
                    checkbox.paintFlags = checkbox.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                } else {
                    checkbox.paintFlags = checkbox.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
                }

                onExerciseCompleted(exercise)
            }
        }
    }

    class ExerciseViewHolder(val binding: ItemExerciseBinding) :
        RecyclerView.ViewHolder(binding.root)
}

private class ExercisesDiffUtil : DiffUtil.ItemCallback<Exercise>() {
    override fun areItemsTheSame(
        oldItem: Exercise,
        newItem: Exercise
    ): Boolean = oldItem.id == newItem.id

    override fun areContentsTheSame(
        oldItem: Exercise,
        newItem: Exercise
    ): Boolean = oldItem == newItem
}