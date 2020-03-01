package com.lounah.vkmc.feature.feature_albums.photos.domain

import com.lounah.vkmc.core.core_vk.model.Photo
import com.lounah.vkmc.feature.feature_albums.photos.ui.recycler.PhotoUi

internal class PhotosMapper : (List<Photo>) -> List<PhotoUi> {

    override fun invoke(photos: List<Photo>): List<PhotoUi> {
        return photos.map {
            PhotoUi(
                uid = it.id,
                albumId = it.albumId,
                path = it.photo
            )
        }
    }
}