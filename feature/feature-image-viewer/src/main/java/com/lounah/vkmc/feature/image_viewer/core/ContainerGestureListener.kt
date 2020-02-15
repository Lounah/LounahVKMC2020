package com.lounah.vkmc.feature.image_viewer.core


import android.content.Context
import com.lounah.vkmc.feature.image_viewer.core.DismissGestureListener.InterceptResult.*

internal class ContainerGestureListener(
    private val context: Context,
    private val contentSizeProvider: ContentSizeProvider,
    private val dismissCallbacks: DismissableCallback
) : () -> DismissGestureListener {

    override fun invoke(): DismissGestureListener =
        DismissGestureListener(context, contentSizeProvider, dismissCallbacks)
            .apply { gestureInterceptor = { IGNORED } }
}