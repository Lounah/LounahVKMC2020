package com.lounah.vkmc.core.core_vk.business.commands.photo

import androidx.annotation.Keep
import com.google.gson.Gson
import com.lounah.vkmc.core.core_vk.business.VKApiCommandWrapper
import com.vk.api.sdk.VKApiResponseParser

internal class VKDeletePhotoCommand(
    photoId: String,
    override val method: String = "photos.delete",
    override val arguments: Map<String, String> = mapOf("photo_id" to photoId),
    override val responseParser: VKApiResponseParser<Boolean> = DeletePhotoResponseParser()
) : VKApiCommandWrapper<Boolean>() {

    private class DeletePhotoResponseParser(
        private val gson: Gson = Gson()
    ) : VKApiResponseParser<Boolean> {

        override fun parse(response: String?): Boolean {
            return gson.fromJson(response, DeletePhotoResponse::class.java).isSucceed
        }
    }
}

@Keep
data class DeletePhotoResponse(val response: Int) {
    val isSucceed: Boolean
        get() = response == 1
}
