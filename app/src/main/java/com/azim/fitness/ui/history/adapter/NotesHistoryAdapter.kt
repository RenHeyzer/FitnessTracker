package com.azim.fitness.ui.history.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.azim.fitness.databinding.ItemNoteHistoryBinding
import com.azim.fitness.db.entity.Note
import com.azim.fitness.utils.toPrettyString

class NotesHistoryAdapter :
    ListAdapter<Note, NotesHistoryAdapter.NoteViewHolder>(NotesHistoryDiffUtil()) {

    inner class NoteViewHolder(private val binding: ItemNoteHistoryBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Note) {
            binding.tvNoteText.text = item.noteText
            binding.tvNoteTime.text = item.timestamp.toPrettyString()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        return NoteViewHolder(
            ItemNoteHistoryBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) =
        holder.bind(getItem(position))
}

private class NotesHistoryDiffUtil : DiffUtil.ItemCallback<Note>() {
    override fun areItemsTheSame(oldItem: Note, newItem: Note) =
        oldItem.noteId == newItem.noteId

    override fun areContentsTheSame(oldItem: Note, newItem: Note) =
        oldItem == newItem
}