package com.lounah.vkmc.feature.image_viewer.ui

import android.animation.ObjectAnimator
import android.graphics.drawable.Drawable
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.interpolator.view.animation.FastOutSlowInInterpolator
import com.lounah.vkmc.core.extensions.animateReveal
import com.lounah.vkmc.core.extensions.animateTranslationY
import com.lounah.vkmc.core.extensions.dp
import com.lounah.vkmc.feature.image_viewer.immersive.SystemUiHelper
import com.lounah.vkmc.feature.image_viewer.immersive.SystemUiHelper.LEVEL_IMMERSIVE
import kotlinx.android.synthetic.main.activity_image_viewer.*
import kotlin.math.abs

private const val REVEAL_DURATION = 250L
private const val IMAGE_DISMISS_DURATION = 250L
private const val DIM_BACKGROUND_DURATION = 450L

internal class ImageViewerUiHelper(
    private val activity: ImageViewerActivity,
    revealX: Int,
    revealY: Int
) {

    private val root = activity.root
    private val toolbar = activity.toolbar
    private val bgDrawable: Drawable = root.background.mutate()
    private val systemUiHelper = SystemUiHelper(activity, LEVEL_IMMERSIVE, 0, null)

    init {
        root.background = bgDrawable
        root.animateReveal(revealX, revealY, REVEAL_DURATION)
        dimBackground()
    }

    fun handleImageMove(moveRatio: Float) {
        updateBackgroundDimmingAlpha(abs(moveRatio))
        systemUiHelper.hide()
        toolbar.animateTranslationY(-toolbar.height - 16.dp(activity), 450, 100)
    }

    fun handleImageDismiss() {
        root.postDelayed(activity::finish, IMAGE_DISMISS_DURATION)
    }

    fun onImageClicked() {
        if (systemUiHelper.isShowing) {
            toolbar.animateTranslationY(-toolbar.height - 16.dp(activity), 450, 100)
            systemUiHelper.hide()
        } else {
            toolbar.animateTranslationY(0, 450)
            systemUiHelper.show()
        }
    }

    private fun dimBackground() {
        ObjectAnimator.ofFloat(1f, 0f).apply {
            duration = DIM_BACKGROUND_DURATION
            interpolator = FastOutSlowInInterpolator()
            addUpdateListener { animation ->
                updateBackgroundDimmingAlpha(animation.animatedValue as Float)
            }
            start()
        }
    }

    private fun updateBackgroundDimmingAlpha(transparencyFactor: Float) {
        val dimming = 1f - 1f.coerceAtMost(transparencyFactor * 2)
        bgDrawable.alpha = (dimming * 255).toInt()
    }
}