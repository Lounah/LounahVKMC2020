package com.lounah.vkmc.feature.image_viewer.core

interface ContentSizeProvider {
    fun heightForDismissAnimation(): Int
    fun heightForCalculatingDismissThreshold(): Int
}