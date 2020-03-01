package com.lounah.vkmc.core.core_vk.business.commands.photo

import com.google.gson.Gson
import com.lounah.vkmc.core.core_vk.business.VKApiCommandWrapper
import com.lounah.vkmc.core.core_vk.domain.Offset
import com.lounah.vkmc.core.core_vk.model.Photo
import com.lounah.vkmc.core.core_vk.model.PhotosResponse
import com.vk.api.sdk.VKApiResponseParser

internal class VKGetPhotosCommand(
    offset: Offset,
    albumId: String,
    override val method: String = "photos.get",
    override val responseParser: VKApiResponseParser<List<Photo>> = GetPhotosResponseParser()
) : VKApiCommandWrapper<List<Photo>>() {

    override val arguments: Map<String, String> = mapOf(
        "offset" to "$offset",
        "photo_sizes" to "1",
        "album_id" to albumId
    )

    private class GetPhotosResponseParser(
        private val gson: Gson = Gson()
    ) : VKApiResponseParser<List<Photo>> {

        override fun parse(response: String?): List<Photo> {
            return gson.fromJson(response, PhotosResponse::class.java).response.items
        }
    }
}