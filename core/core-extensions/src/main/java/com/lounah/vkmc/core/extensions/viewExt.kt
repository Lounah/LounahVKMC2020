@file:Suppress("NOTHING_TO_INLINE")

package com.lounah.vkmc.core.extensions

import android.content.Context
import android.view.View
import android.view.ViewAnimationUtils
import android.view.animation.AccelerateInterpolator
import android.view.inputmethod.InputMethodManager

inline fun View.animateScale(to: Int, duration: Long = 150) {
    animate()
        .scaleX(to.toFloat())
        .scaleY(to.toFloat())
        .withStartAction { isEnabled = false }
        .withEndAction { isEnabled = true }
        .setDuration(duration)
        .start()
}

inline fun View.animateTranslationY(to: Int, duration: Long = 150, startDelay: Long = 0) = animate()
    .translationY(to.toFloat())
    .setDuration(duration)
    .setStartDelay(startDelay)
    .start()

inline fun View.animateAlpha(to: Int, duration: Long = 250) {
    if (isEnabled && alpha != to.toFloat())
        animate()
            .withLayer()
            .withStartAction { isEnabled = false }
            .withEndAction { isEnabled = true }
            .alpha(to.toFloat())
            .setDuration(duration)
            .start()
}

inline fun View.toggleAlpha(duration: Long = 250) {
    if (alpha == 0f) animateAlpha(1, duration) else animateAlpha(0, duration)
}

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

fun View.showForceKeyboard() {
    post {
        this.requestFocus()
        val imm = this.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(this, 0)
    }
}

inline fun View.hide() {
    visibility = View.INVISIBLE
}

inline fun View.gone() {
    visibility = View.GONE
}

fun View.hideKeyboard() {
    post {
        val imm = this.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(this.windowToken, 0)
    }
}
