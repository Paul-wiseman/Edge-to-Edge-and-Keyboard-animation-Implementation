

package com.raywenderlich.android.braindump.ui.compat

import android.content.Context
import android.graphics.Insets
import android.os.Build
import android.os.CancellationSignal
import android.view.*
import android.view.animation.LinearInterpolator
import android.widget.AbsListView
import androidx.annotation.RequiresApi
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updateLayoutParams
import androidx.core.view.updateMargins
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView.*

internal class RWCompat11(private val view: View, private val container: View) {

  private var animationController: WindowInsetsAnimationController? = null

  private var posTop = 0
  private var posBottom = 0

  private val animationControlListener: WindowInsetsAnimationControlListener by lazy {
    @RequiresApi(Build.VERSION_CODES.R)
    object : WindowInsetsAnimationControlListener {

      override fun onReady(
          controller: WindowInsetsAnimationController,
          types: Int
      ) {
        animationController = controller
      }

      override fun onFinished(controller: WindowInsetsAnimationController) {
        animationController = null
      }

      override fun onCancelled(controller: WindowInsetsAnimationController?) {
        animationController = null
      }
    }
  }

  fun setUiWindowInsets() {
    ViewCompat.setOnApplyWindowInsetsListener(container) { _, insets ->

      if (posBottom == 0) {
        posTop = insets.getInsets(WindowInsetsCompat.Type.systemBars()).top
        posBottom = insets.getInsets(WindowInsetsCompat.Type.systemBars()).bottom
      }

      container.updateLayoutParams<ViewGroup.MarginLayoutParams> {
        updateMargins(
            top = posTop,
            bottom = posBottom)
      }

      insets
    }
  }

  @RequiresApi(Build.VERSION_CODES.R)
  fun createLinearLayoutManager(context: Context, view: View): LinearLayoutManager {
    var scrolledY = 0
    var scrollToOpenKeyboard = false

    return object : LinearLayoutManager(context) {

      var visible = false

      override fun onScrollStateChanged(state: Int) {
        if (state == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {

          visible = view.rootWindowInsets?.isVisible(WindowInsetsCompat.Type.ime()) == true

          if (visible) {
            scrolledY = view.rootWindowInsets?.getInsets(WindowInsetsCompat.Type.ime())!!.bottom
          }

          createWindowInsetsAnimation()
        } else if (state == AbsListView.OnScrollListener.SCROLL_STATE_IDLE) {
          scrolledY = 0
          animationController?.finish(scrollToOpenKeyboard)
        }

        super.onScrollStateChanged(state)
      }

      override fun scrollVerticallyBy(dy: Int, recycler: Recycler, state: State): Int {

        scrollToOpenKeyboard = scrolledY < scrolledY + dy

        scrolledY += dy

        if (scrolledY < 0) {
          scrolledY = 0
        }

        animationController?.setInsetsAndAlpha(
            Insets.of(0, 0, 0, scrolledY),
            1f,
            0f
        )

        return super.scrollVerticallyBy(dy, recycler, state)
      }
    }
  }

  @RequiresApi(Build.VERSION_CODES.R)
  private fun createWindowInsetsAnimation() {
    view.windowInsetsController?.controlWindowInsetsAnimation(
        WindowInsetsCompat.Type.ime(),
        -1,
        LinearInterpolator(),
        CancellationSignal(),
        animationControlListener
    )
  }

  @RequiresApi(Build.VERSION_CODES.R)
  fun animateKeyboardDisplay() {
    val cb = object : WindowInsetsAnimation.Callback(DISPATCH_MODE_STOP) {

      override fun onProgress(insets: WindowInsets,
          animations: MutableList<WindowInsetsAnimation>): WindowInsets {

        posBottom = insets.getInsets(WindowInsetsCompat.Type.ime()).bottom +
            insets.getInsets(WindowInsetsCompat.Type.systemBars()).bottom

        container.updateLayoutParams<ViewGroup.MarginLayoutParams> {
          updateMargins(
              top = posTop,
              bottom = posBottom)
        }

        return insets
      }
    }

    container.setWindowInsetsAnimationCallback(cb)
  }
}