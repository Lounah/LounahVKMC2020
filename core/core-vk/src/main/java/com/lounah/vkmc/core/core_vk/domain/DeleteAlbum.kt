package com.lounah.vkmc.core.core_vk.domain

import com.lounah.vkmc.core.core_vk.business.commands.albums.VKDeleteAlbumCommand
import com.vk.api.sdk.VK
import io.reactivex.Single

class DeleteAlbum : (AlbumId) -> Single<Boolean> {

    override fun invoke(albumId: AlbumId): Single<Boolean> {
        return Single.fromCallable { VK.executeSync(VKDeleteAlbumCommand(albumId)) }
    }
}