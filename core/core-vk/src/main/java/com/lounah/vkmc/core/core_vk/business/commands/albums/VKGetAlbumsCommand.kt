package com.lounah.vkmc.core.core_vk.business.commands.albums

import com.lounah.vkmc.core.core_vk.business.VKApiCommandWrapper
import com.lounah.vkmc.core.core_vk.business.gson
import com.lounah.vkmc.core.core_vk.model.Album
import com.lounah.vkmc.core.core_vk.model.AlbumsResponse
import com.vk.api.sdk.VKApiResponseParser

internal class VKGetAlbumsCommand(
    offset: Int,
    override val method: String = "photos.getAlbums",
    override val responseParser: VKApiResponseParser<List<Album>> = GetAlbumsResponseParser()
) : VKApiCommandWrapper<List<Album>>() {

    override val arguments: Map<String, String> = mapOf(
        "offset" to "$offset",
        "need_system" to "1",
        "photo_sizes" to "1",
        "need_covers" to "1"
    )

    private class GetAlbumsResponseParser: VKApiResponseParser<List<Album>> {

        override fun parse(response: String?): List<Album> {
            return gson.fromJson(response, AlbumsResponse::class.java).response.items
        }
    }
}
