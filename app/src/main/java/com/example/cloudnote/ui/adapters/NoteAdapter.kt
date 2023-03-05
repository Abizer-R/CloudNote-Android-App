package com.example.cloudnote.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.cloudnote.data.models.noteModels.NoteResponse
import com.example.cloudnote.databinding.LayoutNoteItemBinding


class NoteAdapter(private val onNoteClicked: (NoteResponse) -> Unit) : ListAdapter<NoteResponse, NoteAdapter.NoteViewHolder>(DiffCallBack()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val binding = LayoutNoteItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return NoteViewHolder(binding)
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        val currentItem = getItem(position)
        holder.bind(currentItem)
    }

    inner class NoteViewHolder(private val binding : LayoutNoteItemBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(noteResponse: NoteResponse) {
            binding.title.text = noteResponse.title
            binding.desc.text = noteResponse.description
            binding.root.setOnClickListener {
                onNoteClicked(noteResponse)
            }
        }
    }

    class DiffCallBack : DiffUtil.ItemCallback<NoteResponse>() {

        override fun areItemsTheSame(oldItem: NoteResponse, newItem: NoteResponse) = oldItem._id == newItem._id

        override fun areContentsTheSame(oldItem: NoteResponse, newItem: NoteResponse) = oldItem == newItem
    }


}