package com.lounah.vkmc.core.core_vk.domain

import com.lounah.vkmc.core.core_vk.business.commands.albums.VKGetAlbumsCommand
import com.lounah.vkmc.core.core_vk.model.Album
import com.vk.api.sdk.VK
import io.reactivex.Single

class GetAlbums : (Offset) -> Single<List<Album>> {

    override fun invoke(offset: Offset): Single<List<Album>> {
        return Single.fromCallable { VK.executeSync(VKGetAlbumsCommand(offset)) }
    }
}