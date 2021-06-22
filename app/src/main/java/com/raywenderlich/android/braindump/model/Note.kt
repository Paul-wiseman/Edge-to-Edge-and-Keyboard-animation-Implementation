

package com.raywenderlich.android.braindump.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Note(val content: String, val timestamp: Long) : Parcelable