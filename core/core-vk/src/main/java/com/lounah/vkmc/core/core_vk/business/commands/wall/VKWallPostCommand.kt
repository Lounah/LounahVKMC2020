package com.lounah.vkmc.core.core_vk.business.commands.wall

import android.net.Uri
import com.google.gson.Gson
import com.lounah.vkmc.core.core_vk.business.commands.photo.VKWallPhotoUploader
import com.lounah.vkmc.core.core_vk.model.WallPostId
import com.lounah.vkmc.core.core_vk.model.WallPostResponse
import com.vk.api.sdk.VKApiManager
import com.vk.api.sdk.VKApiResponseParser
import com.vk.api.sdk.VKMethodCall
import com.vk.api.sdk.internal.ApiCommand

internal class VKWallPostCommand(
    private val comment: String,
    private val attachments: List<Uri>,
    private val vkPhotoUploader: (Uri, VKApiManager) -> String = VKWallPhotoUploader()
) : ApiCommand<WallPostId>() {

    override fun onExecute(manager: VKApiManager): WallPostId {
        val callBuilder = VKMethodCall.Builder()
            .method("wall.post")
            .args("message", comment)
            .version(manager.config.version)

        val photos = attachments.map { vkPhotoUploader(it, manager) }

        callBuilder.args("attachments", photos.joinToString(","))

        return manager.execute(callBuilder.build(), ResponseApiParser())
    }

    private class ResponseApiParser(private val gson: Gson = Gson()) :
        VKApiResponseParser<WallPostId> {
        override fun parse(response: String): WallPostId {
            return gson.fromJson(response, WallPostResponse::class.java).response
        }
    }
}
