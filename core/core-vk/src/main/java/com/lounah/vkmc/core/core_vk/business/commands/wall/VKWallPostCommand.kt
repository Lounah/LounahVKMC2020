package com.lounah.vkmc.core.core_vk.business.commands.wall

import android.net.Uri
import com.google.gson.Gson
import com.lounah.vkmc.core.core_vk.business.commands.photo.VKPhotoUploader
import com.lounah.vkmc.core.core_vk.model.WallPost
import com.vk.api.sdk.VKApiManager
import com.vk.api.sdk.VKApiResponseParser
import com.vk.api.sdk.VKMethodCall
import com.vk.api.sdk.internal.ApiCommand

class VKWallPostCommand(
    private val comment: String,
    private val attachments: List<Uri>,
    private val vkPhotoUploader: (Uri, VKApiManager) -> String = VKPhotoUploader()
) : ApiCommand<WallPost>() {

    override fun onExecute(manager: VKApiManager): WallPost {
        val callBuilder = VKMethodCall.Builder()
            .method("wall.post")
            .args("message", comment)
            .version(manager.config.version)

        val photos = attachments.map { vkPhotoUploader(it, manager) }

        callBuilder.args("attachments", photos.joinToString(","))

        return manager.execute(callBuilder.build(), ResponseApiParser())
    }

    private class ResponseApiParser(private val gson: Gson = Gson()) :
        VKApiResponseParser<WallPost> {
        override fun parse(response: String): WallPost {
            return gson.fromJson(response, WallPost::class.java)
        }
    }
}
