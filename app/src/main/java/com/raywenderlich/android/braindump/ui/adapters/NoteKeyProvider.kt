

package com.raywenderlich.android.braindump.ui.adapters

import androidx.recyclerview.selection.ItemKeyProvider

class NoteKeyProvider(private val adapter: MainAdapter) : ItemKeyProvider<Long>(SCOPE_CACHED) {

  override fun getKey(position: Int): Long? =
      adapter.currentList[position].timestamp

  override fun getPosition(key: Long): Int =
      adapter.currentList.indexOfFirst { it.timestamp == key }
}