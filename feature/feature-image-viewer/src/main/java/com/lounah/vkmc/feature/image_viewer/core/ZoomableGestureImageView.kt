package com.lounah.vkmc.feature.image_viewer.core

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.RectF
import android.util.AttributeSet
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.MotionEvent.ACTION_POINTER_DOWN
import com.alexvasilkov.gestures.GestureController
import com.alexvasilkov.gestures.Settings
import com.alexvasilkov.gestures.State
import com.alexvasilkov.gestures.views.GestureImageView

private const val MAX_OVER_ZOOM = 4F

class ZoomableGestureImageView(context: Context, attrs: AttributeSet) : GestureImageView(context, attrs) {

    private val gestureDetector: GestureDetector
    private val imageMovementRect = RectF()

    init {
        controller.settings.overzoomFactor = MAX_OVER_ZOOM
        controller.settings.isFillViewport = true
        controller.settings.fitMethod = Settings.Fit.HORIZONTAL

        controller.setOnGesturesListener(object : GestureController.SimpleOnGestureListener() {
            override fun onSingleTapConfirmed(event: MotionEvent): Boolean {
                performClick()
                return true
            }

            override fun onUpOrCancel(event: MotionEvent) {
                controller.settings.overzoomFactor = MAX_OVER_ZOOM
            }
        })

        gestureDetector = GestureDetector(context, object : GestureDetector.SimpleOnGestureListener() {
            override fun onDoubleTapEvent(e: MotionEvent): Boolean {
                parent.requestDisallowInterceptTouchEvent(true)
                return super.onDoubleTapEvent(e)
            }
        })
    }

    override fun dispatchTouchEvent(event: MotionEvent): Boolean {
        return drawable != null && super.dispatchTouchEvent(event)
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        gestureDetector.onTouchEvent(event)

        if (event.actionMasked == ACTION_POINTER_DOWN && event.pointerCount == 2) {
            parent.requestDisallowInterceptTouchEvent(true)
        }

        return super.onTouchEvent(event)
    }

    override fun canScrollVertically(direction: Int): Boolean {
        val downwardPan = direction < 0

        val state = controller.state
        controller.stateController.getMovementArea(state, imageMovementRect)

        return (downwardPan.not() && State.compare(state.y, imageMovementRect.bottom) < 0f
                || downwardPan && State.compare(state.y, imageMovementRect.top) > 0f)
    }
}