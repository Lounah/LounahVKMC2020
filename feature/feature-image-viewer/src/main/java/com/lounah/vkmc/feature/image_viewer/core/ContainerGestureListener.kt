package com.lounah.vkmc.feature.image_viewer.core


import android.content.Context
import com.lounah.vkmc.feature.image_viewer.R
import com.lounah.vkmc.feature.image_viewer.core.DismissGestureListener.InterceptResult.IGNORED
import com.lounah.vkmc.feature.image_viewer.core.DismissGestureListener.InterceptResult.INTERCEPTED

internal class ContainerGestureListener(
    private val context: Context,
    private val contentSizeProvider: ContentSizeProvider,
    private val dismissCallbacks: DismissableCallback
) : (ImageDismissLayout) -> DismissGestureListener {

    override fun invoke(view: ImageDismissLayout): DismissGestureListener =
        DismissGestureListener(context, contentSizeProvider, dismissCallbacks)
            .apply {
                gestureInterceptor = {
                    val isScrollingUpwards = it < 0
                    val directionInt = if (isScrollingUpwards) -1 else +1
                    val image = view.findViewById<ZoomableGestureImageView>(R.id.image)
                    val canPanFurther = image.canScrollVertically(directionInt)
                    when {
                        canPanFurther -> INTERCEPTED
                        else -> IGNORED
                    }
                }
            }
}