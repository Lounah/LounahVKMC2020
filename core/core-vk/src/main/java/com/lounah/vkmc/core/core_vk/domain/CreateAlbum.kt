package com.lounah.vkmc.core.core_vk.domain

import com.lounah.vkmc.core.core_vk.business.commands.albums.VKCreateAlbumCommand
import com.vk.api.sdk.VK
import io.reactivex.Single

class CreateAlbumBody(
    val title: String,
    val description: String,
    val isPrivate: Boolean
)

class CreateAlbum : (CreateAlbumBody) -> Single<String> {

    override fun invoke(body: CreateAlbumBody): Single<String> {
        return Single.fromCallable {
            VK.executeSync(VKCreateAlbumCommand(body.title, body.description, body.isPrivate))
        }
    }
}
