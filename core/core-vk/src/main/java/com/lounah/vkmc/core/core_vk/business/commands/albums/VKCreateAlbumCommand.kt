package com.lounah.vkmc.core.core_vk.business.commands.albums

import androidx.annotation.Keep
import com.google.gson.Gson
import com.lounah.vkmc.core.core_vk.business.VKApiCommandWrapper
import com.vk.api.sdk.VKApiResponseParser

internal class VKCreateAlbumCommand(
    title: String,
    description: String,
    isPrivate: Boolean,
    override val method: String = "photos.createAlbum",
    override val responseParser: VKApiResponseParser<String> = CreateAlbumResponseParser()
) : VKApiCommandWrapper<String>() {

    override val arguments: Map<String, String> = mapOf(
        "title" to title,
        "description" to description,
        "privacy_view" to if (isPrivate) "only_me" else "all"
    )

    private class CreateAlbumResponseParser(
        private val gson: Gson = Gson()
    ) : VKApiResponseParser<String> {

        override fun parse(response: String?): String {
            return gson.fromJson(response, CreateAlbumResponse::class.java).response.id
        }
    }
}

@Keep
data class CreateAlbumResponse(val response: CreatedAlbum)

@Keep
data class CreatedAlbum(val id: String)
