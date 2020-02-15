package com.lounah.vkmc.feature.image_viewer.core

interface DismissableCallback {
    fun onFlickDismiss(flickAnimationDuration: Long)
    fun onMove(moveRatio: Float)
}