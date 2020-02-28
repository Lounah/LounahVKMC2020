package com.lounah.vkmc.core.core_vk.business.commands.albums

import com.google.gson.Gson
import com.lounah.vkmc.core.core_vk.business.VKApiCommandWrapper
import com.lounah.vkmc.core.core_vk.domain.AlbumId
import com.vk.api.sdk.VKApiResponseParser

internal class VKDeleteAlbumCommand(
    albumId: AlbumId,
    override val method: String = "photos.deleteAlbum",
    override val arguments: Map<String, String> = mapOf("album_id" to albumId),
    override val responseParser: VKApiResponseParser<Boolean> = DeleteAlbumResponseParser()
) : VKApiCommandWrapper<Boolean>() {

    private class DeleteAlbumResponseParser(
        private val gson: Gson = Gson()
    ) : VKApiResponseParser<Boolean> {

        override fun parse(response: String?): Boolean {
            return gson.fromJson(response, DeleteAlbumResponse::class.java).isSucceed
        }
    }
}

data class DeleteAlbumResponse(val response: Int) {
    val isSucceed: Boolean
        get() = response == 1
}