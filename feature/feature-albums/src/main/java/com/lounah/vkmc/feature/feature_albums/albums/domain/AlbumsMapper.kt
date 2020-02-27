package com.lounah.vkmc.feature.feature_albums.albums.domain

import android.content.Context
import com.lounah.vkmc.core.core_vk.model.Album
import com.lounah.vkmc.feature.feature_albums.R
import com.lounah.vkmc.feature.feature_albums.albums.ui.recycler.holder.AlbumUi

internal class AlbumsMapper(
    private val context: Context
) : (List<Album>) -> List<AlbumUi> {

    override fun invoke(albums: List<Album>): List<AlbumUi> {
        return albums.map { album ->
            AlbumUi(
                uid = album.id,
                thumb = album.photo,
                title = album.title,
                isEditable = album.id.toInt() > 0,
                subtitle = getPhotosQuantity(album.size)
            )
        }
    }

    private fun getPhotosQuantity(albumSize: Int): String {
        return context.resources.getQuantityString(R.plurals.photosCount, albumSize)
    }
}