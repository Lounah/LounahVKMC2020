@file:Suppress("NOTHING_TO_INLINE")

package com.lounah.vkmc.core.extensions

import android.view.View
import android.view.ViewAnimationUtils
import android.view.animation.AccelerateInterpolator

inline fun View.animateScale(to: Int, duration: Long = 150) = animate()
    .scaleX(to.toFloat()).scaleY(to.toFloat())
    .setDuration(duration)
    .start()

inline fun View.animateTranslationY(to: Int, duration: Long = 150, startDelay: Long = 0) = animate()
    .translationY(to.toFloat())
    .setDuration(duration)
    .setStartDelay(startDelay)
    .start()

inline fun View.animateAlpha(to: Int, duration: Long = 250) = animate()
    .withLayer()
    .alpha(to.toFloat())
    .setDuration(duration)
    .start()

inline fun View.animateReveal(startX: Int, startY: Int = 0, duration: Long = 350) {
    measured {
        val finalRadius = width.coerceAtLeast(height) * 1.1f
        val circularReveal =
            ViewAnimationUtils.createCircularReveal(this, startX, startY, 0f, finalRadius)
                .apply {
                    setDuration(duration)
                    interpolator = AccelerateInterpolator()
                }
        circularReveal.start()
    }
}

inline fun View.measured(crossinline action: () -> Unit) {
    if (isLaidOut) {
        action()
    } else {
        addOnLayoutChangeListener(object : View.OnLayoutChangeListener {
            override fun onLayoutChange(
                view: View,
                left: Int,
                top: Int,
                right: Int,
                bottom: Int,
                oldLeft: Int,
                oldTop: Int,
                oldRight: Int,
                oldBottom: Int
            ) {
                removeOnLayoutChangeListener(this)
                action()
            }
        })
    }
}

inline fun View.show() {
    visibility = View.VISIBLE
}

inline fun View.setVisible(isVisible: Boolean) {
    if (isVisible) show() else gone()
}

inline fun List<View>.setVisible(isVisible: Boolean) {
    forEach { if (isVisible) it.show() else it.gone() }
}

inline fun View.hide() {
    visibility = View.INVISIBLE
}

inline fun View.gone() {
    visibility = View.GONE
}