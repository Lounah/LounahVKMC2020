package com.lounah.vkmc.feature.feature_albums.albums.presentation

import com.lounah.vkmc.core.core_vk.domain.AlbumId
import com.lounah.vkmc.core.recycler.base.ViewTyped

sealed class AlbumsAction {
    object OnRepeatLoadClicked : AlbumsAction()
    object LoadAlbums : AlbumsAction()
    object OnEditClicked : AlbumsAction()
    object OnLoadingError : AlbumsAction()
    object OnLoadingStarted : AlbumsAction()
    object OnCancelEditClicked: AlbumsAction()
    class OnAlbumsLoaded(val albums: List<ViewTyped>) : AlbumsAction()
    class OnNextPage(val offset: Int) : AlbumsAction()
    class OnDeleteAlbumClicked(val albumId: AlbumId) : AlbumsAction()
    class OnAlbumDeleted(val albumId: AlbumId) : AlbumsAction()
}
