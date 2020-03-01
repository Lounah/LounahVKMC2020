package com.lounah.vkmc.core.core_vk.domain

import android.net.Uri
import com.lounah.vkmc.core.core_vk.business.commands.photo.UploadPhotoApiCommand
import com.vk.api.sdk.VK
import io.reactivex.Single

class UploadPhoto : (AlbumId, Uri) -> Single<String> {

    override fun invoke(albumId: AlbumId, uri: Uri): Single<String> {
        return Single.create {
            it.onSuccess(VK.executeSync(UploadPhotoApiCommand(albumId, uri)))
        }
    }
}