package com.lounah.vkmc.feature.image_viewer.core

import android.annotation.SuppressLint
import android.content.Context
import android.view.MotionEvent
import android.view.VelocityTracker
import android.view.View
import android.view.ViewConfiguration
import androidx.interpolator.view.animation.FastOutSlowInInterpolator
import java.lang.Math.*

class DismissGestureListener(
    context: Context,
    private val contentHeightProvider: ContentSizeProvider,
    private val flickCallbacks: DismissableCallback
) : View.OnTouchListener {

    var gestureInterceptor: (scrollY: Float) -> InterceptResult = { InterceptResult.IGNORED }
    var flickThresholdSlop = DEFAULT_FLICK_THRESHOLD

    private val viewConfiguration: ViewConfiguration = ViewConfiguration.get(context)
    private val touchSlop: Int = viewConfiguration.scaledTouchSlop
    private val maximumFlingVelocity: Int = viewConfiguration.scaledMaximumFlingVelocity

    private var downX = 0F
    private var downY = 0F
    private var lastTouchX = 0F
    private var lastTouchY = 0F
    private var lastAction = -1
    private var touchStartedOnLeftSide: Boolean = false
    private var velocityTracker: VelocityTracker? = null
    private var verticalScrollRegistered: Boolean = false
    private var gestureCanceledUntilNextTouchDown: Boolean = false
    private var gestureInterceptedUntilNextTouchDown: Boolean = false

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouch(view: View, event: MotionEvent): Boolean {
        val touchX = event.rawX
        val touchY = event.rawY

        val distanceX = touchX - downX
        val distanceY = touchY - downY
        val distanceXAbs = abs(distanceX)
        val distanceYAbs = abs(distanceY)
        val deltaX = touchX - lastTouchX
        val deltaY = touchY - lastTouchY

        if (touchX == lastTouchX && touchY == lastTouchY && lastAction == event.action) {
            return false
        }

        lastTouchX = touchX
        lastTouchY = touchY
        lastAction = event.action

        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                downX = touchX
                downY = touchY
                touchStartedOnLeftSide = touchX < view.width / 2
                velocityTracker = VelocityTracker.obtain()
                velocityTracker!!.addMovement(event)
                return false
            }

            MotionEvent.ACTION_CANCEL, MotionEvent.ACTION_UP -> {
                if (verticalScrollRegistered) {
                    val flickRegistered = hasFingerMovedEnoughToFlick(distanceYAbs)
                    val wasSwipedDownwards = distanceY > 0

                    if (flickRegistered) {
                        animateDismissal(view, wasSwipedDownwards)

                    } else {
                        velocityTracker!!.computeCurrentVelocity(1000)
                        val yVelocityAbs = abs(velocityTracker!!.yVelocity)
                        val requiredYVelocity = view.height * 6 / 10
                        val minSwipeDistanceForFling = view.height / 10

                        if (yVelocityAbs > requiredYVelocity
                            && distanceYAbs >= minSwipeDistanceForFling
                            && yVelocityAbs < maximumFlingVelocity
                        ) {
                            animateDismissal(view, wasSwipedDownwards, 100)

                        } else {
                            animateViewBackToPosition(view)
                        }
                    }
                }

                velocityTracker!!.recycle()
                verticalScrollRegistered = false
                gestureInterceptedUntilNextTouchDown = false
                gestureCanceledUntilNextTouchDown = false
                return false
            }

            MotionEvent.ACTION_MOVE -> {
                if (gestureInterceptedUntilNextTouchDown || gestureCanceledUntilNextTouchDown) {
                    return false
                }

                if (verticalScrollRegistered.not() && gestureInterceptor(distanceY) == InterceptResult.INTERCEPTED) {
                    gestureInterceptedUntilNextTouchDown = true
                    return false
                }

                val isScrollingVertically = distanceYAbs > touchSlop && distanceYAbs > distanceXAbs
                val isScrollingHorizontally =
                    distanceXAbs > touchSlop && distanceYAbs < distanceXAbs

                if (verticalScrollRegistered.not() && isScrollingHorizontally) {
                    gestureCanceledUntilNextTouchDown = true
                    return false
                }

                if (verticalScrollRegistered || isScrollingVertically) {
                    verticalScrollRegistered = true

                    view.translationX = view.translationX + deltaX
                    view.translationY = view.translationY + deltaY

                    view.parent.requestDisallowInterceptTouchEvent(true)

                    val moveRatioDelta =
                        deltaY / view.height * (if (touchStartedOnLeftSide) -20F else 20F)
                    view.pivotY = 0f
                    view.rotation = view.rotation + moveRatioDelta

                    dispatchOnPhotoMoveCallback(view)

                    velocityTracker!!.addMovement(event)
                    return true

                } else {
                    return false
                }
            }

            else -> return false
        }
    }

    private fun dispatchOnPhotoMoveCallback(view: View) {
        val moveRatio = view.translationY / view.height
        flickCallbacks.onMove(moveRatio)
    }

    private fun animateViewBackToPosition(view: View) {
        view.animate().cancel()
        view.animate()
            .translationX(0f)
            .translationY(0f)
            .rotation(0f)
            .setDuration(200)
            .setUpdateListener { dispatchOnPhotoMoveCallback(view) }
            .setInterpolator(ANIM_INTERPOLATOR)
            .start()
    }

    private fun animateDismissal(view: View, downwards: Boolean) {
        animateDismissal(view, downwards, 200)
    }

    private fun animateDismissal(view: View, downwards: Boolean, flickAnimDuration: Long) {
        val rotationAngle = view.rotation
        val distanceRotated =
            ceil(abs(sin(toRadians(rotationAngle.toDouble())) * view.width / 2)).toInt()
        val throwDistance = distanceRotated + max(
            contentHeightProvider.heightForDismissAnimation(),
            view.rootView.height
        )

        view.animate().cancel()
        view.animate()
            .translationY((if (downwards) throwDistance else -throwDistance).toFloat())
            .withStartAction { flickCallbacks.onFlickDismiss(flickAnimDuration) }
            .setDuration(flickAnimDuration)
            .setInterpolator(ANIM_INTERPOLATOR)
            .setUpdateListener { dispatchOnPhotoMoveCallback(view) }
            .start()
    }

    private fun hasFingerMovedEnoughToFlick(distanceYAbs: Float): Boolean {
        val thresholdDistanceY =
            contentHeightProvider.heightForCalculatingDismissThreshold() * flickThresholdSlop
        return distanceYAbs > thresholdDistanceY
    }

    companion object {

        @JvmField
        val ANIM_INTERPOLATOR = FastOutSlowInInterpolator()

        const val DEFAULT_FLICK_THRESHOLD = 0.3f
    }

    enum class InterceptResult {
        INTERCEPTED,
        IGNORED
    }
}