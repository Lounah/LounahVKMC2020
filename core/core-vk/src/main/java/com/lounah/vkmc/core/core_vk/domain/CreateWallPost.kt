package com.lounah.vkmc.core.core_vk.domain

import android.net.Uri
import com.lounah.vkmc.core.core_vk.business.commands.wall.VKWallPostCommand
import com.lounah.vkmc.core.core_vk.model.WallPost
import com.vk.api.sdk.VK
import io.reactivex.Single

class CreateWallPost : (String, List<Uri>) -> Single<WallPost> {

    override fun invoke(comment: String, attachments: List<Uri>): Single<WallPost> {
        return Single.fromCallable { VK.executeSync(VKWallPostCommand(comment, attachments)) }
    }
}
