package com.lounah.vkmc.core.core_vk.business.commands.photo

import com.google.gson.Gson
import com.lounah.vkmc.core.core_vk.business.VKApiCommandWrapper
import com.lounah.vkmc.core.core_vk.business.gson
import com.lounah.vkmc.core.core_vk.domain.groups.Offset
import com.lounah.vkmc.core.core_vk.model.Photo
import com.lounah.vkmc.core.core_vk.model.PhotoByLatLng
import com.lounah.vkmc.core.core_vk.model.PhotosByLatLngResponse
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

internal class VKSearchPhotosCommand(
    latLng: Pair<Double, Double>,
    override val method: String = "photos.search",
    override val responseParser: VKApiResponseParser<List<PhotoByLatLng>> = VKGetPhotosResponseParser()
) : VKApiCommandWrapper<List<PhotoByLatLng>>() {

    override val arguments: Map<String, String> = mapOf(
        "lat" to "${latLng.first}",
        "long" to "${latLng.second}",
        "count" to "100"
    )

    private class VKGetPhotosResponseParser : VKApiResponseParser<List<PhotoByLatLng>> {
        override fun parse(response: String?): List<PhotoByLatLng> {
            return gson.fromJson(response, PhotosByLatLngResponse::class.java).response.items
        }
    }
}
