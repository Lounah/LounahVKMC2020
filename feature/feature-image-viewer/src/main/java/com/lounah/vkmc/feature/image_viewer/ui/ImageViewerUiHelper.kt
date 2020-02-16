package com.lounah.vkmc.feature.image_viewer.ui

import android.animation.ObjectAnimator
import android.graphics.drawable.Drawable
import androidx.interpolator.view.animation.FastOutSlowInInterpolator
import com.lounah.vkmc.core.extensions.animateAlpha
import com.lounah.vkmc.core.extensions.animateReveal
import com.lounah.vkmc.core.extensions.toggleAlpha
import kotlinx.android.synthetic.main.activity_image_viewer.*
import kotlin.math.abs

private const val REVEAL_DURATION = 350L
private const val IMAGE_DISMISS_DURATION = 250L
private const val DIM_BACKGROUND_DURATION = 450L

internal class ImageViewerUiHelper(
    private val activity: ImageViewerActivity,
    private val isForSinglePick: Boolean
) {

    private val root = activity.root
    private val toolbar = activity.toolbar
    private val buttonContainer = activity.buttonContainer
    private val bgDrawable: Drawable = root.background.mutate()

    init {
        root.background = bgDrawable
        val revealX = activity.resources.displayMetrics.widthPixels / 2
        val revealY = activity.resources.displayMetrics.heightPixels / 2
        root.animateReveal(revealX, revealY, REVEAL_DURATION)
        dimBackground()
    }

    fun handleImageMove(moveRatio: Float) {
        updateBackgroundDimmingAlpha(abs(moveRatio))
        toolbar.animateAlpha(0)
        if (isForSinglePick)
            buttonContainer.animateAlpha(0)
    }

    fun handleImageDismiss() {
        root.postDelayed(activity::finish, IMAGE_DISMISS_DURATION)
    }

    fun onImageClicked() {
        toolbar.toggleAlpha()
        if (isForSinglePick)
            buttonContainer.toggleAlpha()
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
