

package com.raywenderlich.android.braindump.ui.compat

import android.content.Context
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.raywenderlich.android.braindump.isAtLeastAndroid11

class RWCompat(private val view: View, private val container: View) : RWCompatActions {

  private val rwCompat10 by lazy { RWCompat10() }
  private val rwCompat11 by lazy { RWCompat11(view, container) }

  /**
   * This method should be called when [androidx.core.view.WindowCompat.setDecorFitsSystemWindows]
   * is set as false. Which means it's the app responsibility to handle the system UI. Since
   * keyboard animations are only available for devices that are running Android 11. Only
   * [RWCompat11] defines this behaviour.
   */
  override fun setupUiWindowInsets() {
    if (isAtLeastAndroid11()) {
      rwCompat11.setUiWindowInsets()
    }
  }

  /**
   * In case the app is running on a device with Android 11 the keyboard will animate seamlessly
   * with the rest of the UI. If not, the behaviour is just popup the screen and afterwards the
   * UI is updated.
   */
  override fun setupKeyboardAnimations() {
    if (isAtLeastAndroid11()) {
      rwCompat11.animateKeyboardDisplay()
    }
  }

  /**
   * Returns an instance of [androidx.recyclerview.widget.LinearLayoutManager] to be used in a
   * [androidx.recyclerview.widget.RecyclerView]. In case the app is running on an Android 11
   * device it will also set a pre-built animation with the keyboard (ime).
   *
   * If user scrolls the list to the top, the keyboard will start to be displayed following the
   * same speed as the user movement. If the scroll is done in the opposite direction, it will
   * close the keyboard.
   *
   */
  override fun createLinearLayoutManager(context: Context): LinearLayoutManager {
    return if (isAtLeastAndroid11()) {
      rwCompat11.createLinearLayoutManager(context, view)
    } else {
      rwCompat10.createLinearLayoutManager(context)
    }
  }
}