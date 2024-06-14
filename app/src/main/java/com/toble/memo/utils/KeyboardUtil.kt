package com.toble.memo.utils

import android.graphics.Rect
import android.util.TypedValue
import android.view.ViewGroup
import android.view.ViewTreeObserver

class KeyboardUtil {
    interface OnKeyboardVisibilityListener {
        fun onVisibilityChanged(isVisible: Boolean)
    }
    companion object {
        fun setKeyboardVisibilityListener(parentView: ViewGroup, onKeyboardVisibilityListener: OnKeyboardVisibilityListener) {
            parentView.viewTreeObserver.addOnGlobalLayoutListener(object :
                ViewTreeObserver.OnGlobalLayoutListener {
                private var alreadyOpen = false
                private val defaultKeyboardHeightDP = 100
                private val EstimatedKeyboardDP = defaultKeyboardHeightDP + 48
                private val rect = Rect()
                override fun onGlobalLayout() {
                    val estimatedKeyboardHeight = TypedValue.applyDimension(
                        TypedValue.COMPLEX_UNIT_DIP,
                        EstimatedKeyboardDP.toFloat(),
                        parentView.resources.displayMetrics
                    ).toInt()
                    parentView.getWindowVisibleDisplayFrame(rect)
                    val heightDiff = parentView.rootView.height - (rect.bottom - rect.top)
                    val isShown = heightDiff >= estimatedKeyboardHeight
                    if (isShown == alreadyOpen) {
                        return
                    }
                    alreadyOpen = isShown
                    onKeyboardVisibilityListener.onVisibilityChanged(isShown)
                }
            })
        }
    }
}