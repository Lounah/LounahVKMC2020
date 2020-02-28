package com.lounah.vkmc.feature.feature_albums.albums.presentation

import com.lounah.vkmc.core.recycler.base.ViewTyped
import com.lounah.vkmc.core.recycler.base.items.ErrorView
import com.lounah.vkmc.core.recycler.base.items.ProgressItem
import com.lounah.vkmc.core.recycler.paging.core.EmptyContentUi
import com.lounah.vkmc.core.recycler.paging.core.PagedErrorUi
import com.lounah.vkmc.core.recycler.paging.core.PagedProgressUi
import com.lounah.vkmc.feature.feature_albums.R
import com.lounah.vkmc.feature.feature_albums.albums.presentation.AlbumsAction.*
import com.lounah.vkmc.feature.feature_albums.albums.ui.recycler.holder.AlbumUi

data class AlbumsState(
    val inEditMode: Boolean = false,
    val loading: Boolean = false,
    val offset: Int = 0,
    val albums: List<ViewTyped> = emptyList(),
    val errorView: ViewTyped = ErrorView(),
    val pagedProgress: ViewTyped = PagedProgressUi(),
    val pagedError: ViewTyped = PagedErrorUi(),
    val emptyView: ViewTyped = EmptyContentUi(R.string.no_albums)
)

internal fun AlbumsState.reduce(action: AlbumsAction): AlbumsState {
    return when (action) {
        is OnEditClicked -> {
            val newItems = albums.toMutableList().map {
                if (it is AlbumUi) {
                    it.copy(isInEditMode = true)
                } else it
            }.toList()
            copy(inEditMode = true, albums = newItems)
        }
        is OnCancelEditClicked -> {
            val newItems = albums.toMutableList().map {
                if (it is AlbumUi) {
                    it.copy(isInEditMode = false)
                } else it
            }.toList()
            copy(inEditMode = false, albums = newItems)
        }
        is OnAlbumDeleted -> {
            copy(albums = albums.filterNot { it.uid == action.albumId })
        }
        is OnNextPage -> copy(offset = action.offset)
        is OnAlbumsLoaded -> {
            val newItems =
                (albums - ProgressItem - errorView - pagedProgress - pagedError - emptyView) + action.albums - ProgressItem
            if (newItems.isEmpty())
                copy(albums = listOf(emptyView))
            else
                copy(albums = newItems)
        }
        is OnLoadingStarted -> {
            val newItems = if (offset == 0 && albums.isEmpty()) listOf(ProgressItem) else {
                (albums - errorView - pagedError - ProgressItem - pagedProgress - emptyView) + pagedProgress
            }.toList()
            copy(albums = newItems)
        }
        is OnLoadingError -> {
            val newItems = when (offset) {
                0 -> listOf(errorView)
                else -> albums - pagedError + pagedError - pagedProgress - emptyView
            }
            copy(albums = newItems)
        }
        else -> this
    }
}