package com.lounah.vkmc.feature.feature_albums.createalbum.presentation

sealed class CreateAlbumAction {
    class OnTitleChanged(val title: String) : CreateAlbumAction()
    class OnDescriptionChanged(val description: String) : CreateAlbumAction()
    class OnPrivacyChanged(val isPrivate: Boolean) : CreateAlbumAction()
    object OnCreateClicked : CreateAlbumAction()
    object OnCreateFailed : CreateAlbumAction()
}
