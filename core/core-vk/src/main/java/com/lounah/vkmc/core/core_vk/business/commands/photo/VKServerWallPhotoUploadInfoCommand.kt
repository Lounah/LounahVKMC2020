package com.lounah.vkmc.core.core_vk.business.commands.photo

import com.lounah.vkmc.core.core_vk.business.VKApiCommandWrapper
import com.lounah.vkmc.core.core_vk.business.gson
import com.lounah.vkmc.core.core_vk.domain.AlbumId
import com.lounah.vkmc.core.core_vk.model.VKServerUploadInfo
import com.lounah.vkmc.core.core_vk.model.VKServerUploadInfoResponse
import com.vk.api.sdk.VKApiResponseParser

internal class VKServerWallPhotoUploadInfoCommand(
    override val method: String = "photos.getWallUploadServer",
    override val arguments: Map<String, String> = emptyMap(),
    override val responseParser: VKApiResponseParser<VKServerUploadInfo> = ServerUploadInfoParser()
) : VKApiCommandWrapper<VKServerUploadInfo>() {

    private class ServerUploadInfoParser : VKApiResponseParser<VKServerUploadInfo> {
        override fun parse(response: String): VKServerUploadInfo {
            return gson.fromJson(response, VKServerUploadInfoResponse::class.java).response
        }
    }
}

internal class VKServerPhotoUploadInfoCommand(
    albumId: AlbumId,
    override val method: String = "photos.getUploadServer",
    override val arguments: Map<String, String> = mapOf("album_id" to albumId),
    override val responseParser: VKApiResponseParser<VKServerUploadInfo> = ServerUploadInfoParser()
) : VKApiCommandWrapper<VKServerUploadInfo>() {

    private class ServerUploadInfoParser : VKApiResponseParser<VKServerUploadInfo> {
        override fun parse(response: String): VKServerUploadInfo {
            return gson.fromJson(response, VKServerUploadInfoResponse::class.java).response
        }
    }
}
