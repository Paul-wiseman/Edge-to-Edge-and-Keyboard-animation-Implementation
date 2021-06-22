

package com.raywenderlich.android.braindump.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import com.raywenderlich.android.braindump.R
import com.raywenderlich.android.braindump.isAtLeastAndroid11

/**
 * Main Screen
 */
class MainActivity : AppCompatActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    // Switch to AppTheme for displaying the activity
    setTheme(R.style.AppTheme)

    WindowCompat.setDecorFitsSystemWindows(window, !isAtLeastAndroid11())

    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)
  }
}
