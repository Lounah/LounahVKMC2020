package com.lounah.vkmc.feature.feature_albums.photos.presentation

import com.lounah.vkmc.core.recycler.base.ViewTyped
import com.lounah.vkmc.core.recycler.base.items.ErrorView
import com.lounah.vkmc.core.recycler.base.items.ProgressItem
import com.lounah.vkmc.core.recycler.paging.core.PagedErrorUi
import com.lounah.vkmc.core.recycler.paging.core.PagedProgressUi
import com.lounah.vkmc.feature.feature_albums.photos.presentation.PhotosAction.*
import com.lounah.vkmc.feature.feature_albums.photos.ui.recycler.AlbumHeaderUi

data class PhotosState(
    val albumName: String = "",
    val albumId: String = "",
    val photos: List<ViewTyped> = emptyList(),
    val offset: Int = 0,
    val errorView: ViewTyped = ErrorView(),
    val pagedProgress: ViewTyped = PagedProgressUi(),
    val pagedError: ViewTyped = PagedErrorUi(),
    val titleItem: ViewTyped = AlbumHeaderUi(albumName)
)

internal fun PhotosState.reduce(action: PhotosAction): PhotosState {
    return when (action) {
        is OnNextPage -> copy(offset = action.offset)
        is OnPhotosLoaded -> {
            val newItems =
                listOf(titleItem) + (photos - ProgressItem - errorView - pagedProgress - pagedError - titleItem) +
                       action.photos - ProgressItem
            copy(photos = newItems)
        }
        is OnLoadingStarted -> {
            val newItems = if (offset == 0) listOf(ProgressItem) else {
                (photos - errorView - pagedError - ProgressItem - pagedProgress) + pagedProgress
            }.toList()
            copy(photos = newItems)
        }
        is OnLoadingError -> {
            val newItems = when (offset) {
                0 -> listOf(errorView)
                else -> photos - pagedError + pagedError - pagedProgress
            }
            copy(photos = newItems)
        }
        else -> this
    }
}