

package com.raywenderlich.android.braindump.ui.adapters

import android.view.MotionEvent
import androidx.recyclerview.selection.ItemDetailsLookup
import androidx.recyclerview.widget.RecyclerView

class NoteItemDetailsLookup(private val recyclerView: RecyclerView) : ItemDetailsLookup<Long>() {

  override fun getItemDetails(event: MotionEvent): ItemDetails<Long>? {
    val view = recyclerView.findChildViewUnder(event.x, event.y)
    if (view != null) {
      return (recyclerView.getChildViewHolder(view) as MainAdapter.NotesViewHolder).getNote()
    }
    return null
  }
}