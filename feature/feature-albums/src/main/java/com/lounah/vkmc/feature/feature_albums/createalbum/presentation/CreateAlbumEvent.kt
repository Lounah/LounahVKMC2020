package com.lounah.vkmc.feature.feature_albums.createalbum.presentation

sealed class CreateAlbumEvent {
    object ShowError : CreateAlbumEvent()
    object OnCreateSucceed : CreateAlbumEvent()
}
