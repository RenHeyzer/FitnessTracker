package com.azim.fitness.ui.history.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.azim.fitness.databinding.ItemWeightHistoryBinding
import com.azim.fitness.db.entity.Weight
import com.azim.fitness.utils.toPrettyString

class WeightHistoryAdapter :
    ListAdapter<Weight, WeightHistoryAdapter.WeightViewHolder>(WeightHistoryDiffUtil()) {

    inner class WeightViewHolder(private val binding: ItemWeightHistoryBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Weight) {
            binding.tvWeightValue.text = item.weight.toString()
            binding.tvWeightTime.text = item.timestamp.toPrettyString()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeightViewHolder {
        return WeightViewHolder(
            ItemWeightHistoryBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: WeightViewHolder, position: Int) =
        holder.bind(getItem(position))
}

private class WeightHistoryDiffUtil : DiffUtil.ItemCallback<Weight>() {
    override fun areItemsTheSame(oldItem: Weight, newItem: Weight) =
        oldItem.weightId == newItem.weightId

    override fun areContentsTheSame(oldItem: Weight, newItem: Weight) =
        oldItem == newItem
}