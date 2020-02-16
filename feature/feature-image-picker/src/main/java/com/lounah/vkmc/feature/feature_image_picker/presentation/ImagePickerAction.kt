package com.lounah.vkmc.feature.feature_image_picker.presentation

import com.lounah.vkmc.core.recycler.base.ReduxAction

sealed class ImagePickerAction : ReduxAction {
    object RequestGalleryPhotos : ImagePickerAction()
    object OnPhotoPickerClicked : ImagePickerAction()
    class OnImageSelected(val path: String) : ImagePickerAction()
    class OnImagesLoaded(val images: List<String>) : ImagePickerAction()
}