package com.lounah.vkmc.feature.feature_albums.albums.presentation

sealed class AlbumsEvent {
    object ErrorDeletingAlbum : AlbumsEvent()
}