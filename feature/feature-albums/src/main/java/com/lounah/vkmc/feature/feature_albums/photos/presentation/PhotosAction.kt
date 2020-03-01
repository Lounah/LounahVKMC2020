package com.lounah.vkmc.feature.feature_albums.photos.presentation

import com.lounah.vkmc.core.recycler.base.ViewTyped

sealed class PhotosAction {
    object OnRepeatLoadClicked : PhotosAction()
    object OnLoadingError : PhotosAction()
    object OnLoadingStarted : PhotosAction()
    class OnPhotoSelected(val photoPath: String) : PhotosAction()
    class OnPhotoUploaded(val id: String, val photo: String) : PhotosAction()
    class OnPhotosLoaded(val photos: List<ViewTyped>) : PhotosAction()
    class OnNextPage(val offset: Int) : PhotosAction()
}