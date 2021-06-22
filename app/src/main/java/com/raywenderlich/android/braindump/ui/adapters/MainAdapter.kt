

package com.raywenderlich.android.braindump.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.selection.ItemDetailsLookup
import androidx.recyclerview.selection.SelectionTracker
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.raywenderlich.android.braindump.R
import com.raywenderlich.android.braindump.databinding.ItemNoteBinding
import com.raywenderlich.android.braindump.model.Note
import com.raywenderlich.android.braindump.timestampToDateConversion

class MainAdapter : ListAdapter<Note, MainAdapter.NotesViewHolder>(
    DiffCallback()) {

  private lateinit var binding: ItemNoteBinding
  var tracker: SelectionTracker<Long>? = null

  override fun onCreateViewHolder(viewGroup: ViewGroup, position: Int): NotesViewHolder {
    binding = ItemNoteBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)
    return NotesViewHolder(binding)
  }

  override fun onBindViewHolder(viewHolder: NotesViewHolder, position: Int) {
    val note = getItem(position)
    viewHolder.bind(note)
  }

  override fun getItemCount(): Int {
    return currentList.size
  }

  private class DiffCallback : DiffUtil.ItemCallback<Note>() {

    override fun areItemsTheSame(oldItem: Note, newItem: Note) =
        oldItem.timestamp == newItem.timestamp

    override fun areContentsTheSame(oldItem: Note, newItem: Note) =
        oldItem == newItem
  }

  inner class NotesViewHolder(private val itemBinding: ItemNoteBinding) : RecyclerView.ViewHolder
  (itemBinding.root) {

    fun bind(note: Note) {
      itemBinding.tvNote.text = note.content
      itemBinding.tvTimestamp.text = timestampToDateConversion(note.timestamp)

      tracker?.let {

        if (it.isSelected(note.timestamp)) {
          itemBinding.rlContainer.setBackgroundColor(
              ContextCompat.getColor(itemBinding.rlContainer.context, R.color.colorPrimary60))
        } else {
          itemBinding.rlContainer.background = null
        }
      }
    }

    fun getNote(): ItemDetailsLookup.ItemDetails<Long> =

        object : ItemDetailsLookup.ItemDetails<Long>() {

          override fun getPosition(): Int = adapterPosition

          override fun getSelectionKey(): Long = getItem(adapterPosition).timestamp
        }
  }
}