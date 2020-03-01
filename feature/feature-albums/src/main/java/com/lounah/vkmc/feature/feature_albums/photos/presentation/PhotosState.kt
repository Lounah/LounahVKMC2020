package com.lounah.vkmc.feature.feature_albums.photos.presentation

import com.lounah.vkmc.core.recycler.base.ViewTyped
import com.lounah.vkmc.core.recycler.base.items.ErrorView
import com.lounah.vkmc.core.recycler.base.items.ProgressItem
import com.lounah.vkmc.core.recycler.paging.core.EmptyContentUi
import com.lounah.vkmc.core.recycler.paging.core.PagedErrorUi
import com.lounah.vkmc.core.recycler.paging.core.PagedProgressUi
import com.lounah.vkmc.feature.feature_albums.R
import com.lounah.vkmc.feature.feature_albums.photos.presentation.PhotosAction.*
import com.lounah.vkmc.feature.feature_albums.photos.ui.recycler.AlbumHeaderUi
import com.lounah.vkmc.feature.feature_albums.photos.ui.recycler.PhotoUi

data class PhotosState(
    val albumName: String = "",
    val albumId: String = "",
    val photos: List<ViewTyped> = emptyList(),
    val offset: Int = 0,
    val errorView: ViewTyped = ErrorView(),
    val pagedProgress: ViewTyped = PagedProgressUi(),
    val emptyView: ViewTyped = EmptyContentUi(R.string.no_photos_in_album),
    val pagedError: ViewTyped = PagedErrorUi(),
    val titleItem: ViewTyped = AlbumHeaderUi(albumName)
)

internal fun PhotosState.reduce(action: PhotosAction): PhotosState {
    return when (action) {
        is OnNextPage -> copy(offset = action.offset)
        is OnPhotosLoaded -> {
            val newItems =
                listOf(titleItem) + (photos - ProgressItem - errorView - pagedProgress - pagedError - titleItem) +
                        action.photos - ProgressItem - emptyView
            if (newItems.filterIsInstance<PhotoUi>().isEmpty()) {
                copy(photos = listOf(titleItem) + emptyView)
            } else {
                copy(photos = newItems)
            }
        }
        is OnLoadingStarted -> {
            val newItems =
                if (offset < 50 && photos.filterIsInstance<PhotoUi>().isEmpty()) listOf(ProgressItem) else {
                    (photos - errorView - pagedError - ProgressItem - pagedProgress - emptyView) + pagedProgress
                }.toList()
            copy(photos = newItems)
        }
        is OnPhotoDeleted -> {
            val newItems = photos.filterNot { it.uid == action.id }
            if (newItems.filterIsInstance<PhotoUi>().isEmpty()) {
                copy(photos = listOf(titleItem) + emptyView)
            } else {
                copy(photos = newItems)
            }
        }
        is OnPhotoUploaded -> {
            copy(
                photos = photos - emptyView - ProgressItem - errorView - pagedError - pagedProgress +
                        PhotoUi(action.id, action.photo, albumId, true)
            )
        }
        is OnLoadingError -> {
            val newItems = when {
                offset < 50 && photos.filterIsInstance<PhotoUi>().isEmpty() -> listOf(titleItem) + errorView
                else -> {
                    listOf(titleItem) + photos - pagedError - pagedProgress - emptyView - errorView - ProgressItem + pagedError
                }
            }
            copy(photos = newItems)
        }
        else -> this
    }
}
