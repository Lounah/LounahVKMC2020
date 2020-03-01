package com.lounah.vkmc.feature.feature_albums.photos.presentation

sealed class PhotosEvent {
    object ShowUploadDialog : PhotosEvent()
    object HideUploadDialog : PhotosEvent()
    object ShowError : PhotosEvent()
    object ErrorDeletePhoto : PhotosEvent()
}
