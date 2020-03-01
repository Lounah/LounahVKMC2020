package com.lounah.vkmc.core.core_vk.domain.wall

import android.net.Uri
import android.os.Parcelable
import com.lounah.vkmc.core.core_vk.business.commands.user.VKGetUserCommand
import com.lounah.vkmc.core.core_vk.business.commands.wall.VKWallPostCommand
import com.vk.api.sdk.VK
import io.reactivex.Single
import kotlinx.android.parcel.Parcelize

@Parcelize
data class WallPost(
    val id: String,
    val userId: String
) : Parcelable

class CreateWallPost : (String, List<Uri>) -> Single<WallPost> {

    override fun invoke(comment: String, attachments: List<Uri>): Single<WallPost> {
        return Single.fromCallable {
            val postId = VK.executeSync(VKWallPostCommand(comment, attachments))
            val userId = VK.executeSync(VKGetUserCommand())
            WallPost(postId.id, userId.id)
        }
    }
}
