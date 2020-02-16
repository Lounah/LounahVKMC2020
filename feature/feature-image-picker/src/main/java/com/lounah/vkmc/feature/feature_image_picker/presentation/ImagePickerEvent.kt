package com.lounah.vkmc.feature.feature_image_picker.presentation

sealed class ImagePickerEvent {
    class ShowError(val error: Throwable) : ImagePickerEvent()
    object RequestCameraAccess : ImagePickerEvent()
    class ShowSelectedImage(val path: String) : ImagePickerEvent()
}