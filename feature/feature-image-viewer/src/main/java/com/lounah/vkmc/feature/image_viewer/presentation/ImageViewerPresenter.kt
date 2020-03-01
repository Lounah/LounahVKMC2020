package com.lounah.vkmc.feature.image_viewer.presentation

import com.lounah.vkmc.core.core_vk.domain.AlbumId
import com.lounah.vkmc.core.core_vk.domain.GetPhotos
import com.lounah.vkmc.core.core_vk.domain.groups.Offset
import com.lounah.vkmc.core.core_vk.model.Photo
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers.single

class ImageViewerPresenter(
    private val albumId: AlbumId,
    private val getPhotos: (Offset, AlbumId) -> Single<List<Photo>> = GetPhotos()
) {

    fun requestPhotos(offset: Offset): Single<List<String>> {
        return getPhotos(offset, albumId)
            .subscribeOn(single())
            .onErrorReturnItem(emptyList())
            .map { it.map(Photo::photo) }
    }
}
