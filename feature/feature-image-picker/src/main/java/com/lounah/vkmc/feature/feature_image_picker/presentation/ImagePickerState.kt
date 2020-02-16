package com.lounah.vkmc.feature.feature_image_picker.presentation

import com.lounah.vkmc.core.recycler.base.ViewTyped
import com.lounah.vkmc.feature.feature_image_picker.presentation.ImagePickerAction.OnImageSelected
import com.lounah.vkmc.feature.feature_image_picker.presentation.ImagePickerAction.OnImagesLoaded
import com.lounah.vkmc.feature.feature_image_picker.ui.viewholders.GalleryImageUi

data class ImagePickerState(
    val selectedImagePath: String? = null,
    val galleryPhotos: List<ViewTyped> = emptyList()
)

internal fun ImagePickerState.reduce(action: ImagePickerAction): ImagePickerState {
    return when (action) {
        is OnImageSelected -> {
            when (selectedImagePath) {
                action.path -> copy(selectedImagePath = null)
                else -> copy(selectedImagePath = action.path)
            }
        }
        is OnImagesLoaded -> copy(galleryPhotos = action.images.map { GalleryImageUi(it) })
        else -> this
    }
}