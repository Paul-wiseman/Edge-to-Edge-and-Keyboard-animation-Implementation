

package com.raywenderlich.android.braindump

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import androidx.preference.PreferenceManager
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.raywenderlich.android.braindump.model.Note
import java.text.SimpleDateFormat
import java.util.*


@SuppressLint("ConstantLocale")
private val dataFormat = SimpleDateFormat("dd/MM/yy hh:mm a", Locale.getDefault())

private const val ALL_NOTES = "all_notes"

fun isAtLeastAndroid11() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.R

fun timestampToDateConversion(timestamp: Long): String {
  val calendar = Calendar.getInstance()
  calendar.timeInMillis = timestamp
  return dataFormat.format(calendar.timeInMillis)
}

fun saveAllNotes(context: Context, list: List<Note>) {
  val json = Gson().toJson(list)

  val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
  sharedPreferences.edit().putString(ALL_NOTES, json).apply()
}

fun getAllNotes(context: Context): List<Note> {
  val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
  val json = sharedPreferences.getString(ALL_NOTES, "")

  if (json.isNullOrEmpty()) {
    return emptyList()
  }

  val type = object : TypeToken<ArrayList<Note>>() {}.type
  return Gson().fromJson(json, type)
}