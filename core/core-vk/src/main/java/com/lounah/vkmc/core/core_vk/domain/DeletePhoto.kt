package com.lounah.vkmc.core.core_vk.domain

import com.lounah.vkmc.core.core_vk.business.commands.photo.VKDeletePhotoCommand
import com.vk.api.sdk.VK
import io.reactivex.Single

class DeletePhoto : (String) -> Single<Boolean> {

    override fun invoke(albumId: AlbumId): Single<Boolean> {
        return Single.fromCallable { VK.executeSync(VKDeletePhotoCommand(albumId)) }
    }
}
