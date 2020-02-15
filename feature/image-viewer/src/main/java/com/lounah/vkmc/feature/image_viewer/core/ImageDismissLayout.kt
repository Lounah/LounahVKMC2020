package com.lounah.vkmc.feature.image_viewer.core

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.widget.FrameLayout

class ImageDismissLayout(context: Context, attrs: AttributeSet) : FrameLayout(context, attrs) {

    var gestureListener: DismissGestureListener? = null

    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
        val intercepted = requireGestureListener().onTouch(this, ev)
        return intercepted || super.onInterceptTouchEvent(ev)
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        requireGestureListener().onTouch(this, event)
        return true
    }

    private fun requireGestureListener(): DismissGestureListener {
        return gestureListener ?: error("Did you forget to set gestureListener?")
    }
}