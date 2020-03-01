package com.lounah.vkmc.core.core_vk.domain

import com.lounah.vkmc.core.core_vk.business.commands.photo.VKGetPhotosCommand
import com.lounah.vkmc.core.core_vk.model.Photo
import com.vk.api.sdk.VK
import io.reactivex.Single

typealias AlbumId = String

class GetPhotos : (Offset, AlbumId) -> Single<List<Photo>> {

    override fun invoke(offset: Offset, albumId: AlbumId): Single<List<Photo>> {
        return Single.fromCallable { VK.executeSync(VKGetPhotosCommand(offset, albumId)) }
    }
}