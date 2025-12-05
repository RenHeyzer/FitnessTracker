package com.azim.fitness.ui.history.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.azim.fitness.databinding.ItemCaloriesHistoryBinding
import com.azim.fitness.db.entity.Food
import com.azim.fitness.utils.toPrettyString

class CaloriesHistoryAdapter :
    ListAdapter<Food, CaloriesHistoryAdapter.FoodViewHolder>(CaloriesHistoryDiffUtil()) {

    inner class FoodViewHolder(private val binding: ItemCaloriesHistoryBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Food) {
            binding.tvFoodName.text = item.foodName
            binding.tvFoodCalories.text = item.calories.toString()
            binding.tvFoodTime.text = item.timestamp.toPrettyString()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FoodViewHolder {
        return FoodViewHolder(
            ItemCaloriesHistoryBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: FoodViewHolder, position: Int) =
        holder.bind(getItem(position))
}

private class CaloriesHistoryDiffUtil : DiffUtil.ItemCallback<Food>() {
    override fun areItemsTheSame(oldItem: Food, newItem: Food) =
        oldItem.foodId == newItem.foodId

    override fun areContentsTheSame(oldItem: Food, newItem: Food) =
        oldItem == newItem
}