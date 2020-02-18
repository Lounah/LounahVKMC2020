package com.lounah.vkmc.core.core_vk.business.commands.photo

import com.google.gson.Gson
import com.lounah.vkmc.core.core_vk.business.VKApiCommandWrapper
import com.lounah.vkmc.core.core_vk.model.VKServerUploadInfo
import com.lounah.vkmc.core.core_vk.model.VKServerUploadInfoResponse
import com.vk.api.sdk.VKApiResponseParser

internal class VKServerUploadInfoCommand(
    override val method: String = "photos.getWallUploadServer",
    override val arguments: Map<String, String> = emptyMap(),
    override val responseParser: VKApiResponseParser<VKServerUploadInfo> = ServerUploadInfoParser()
) : VKApiCommandWrapper<VKServerUploadInfo>() {

    private class ServerUploadInfoParser(
        private val gson: Gson = Gson()
    ) : VKApiResponseParser<VKServerUploadInfo> {
        override fun parse(response: String): VKServerUploadInfo {
            return gson.fromJson(response, VKServerUploadInfoResponse::class.java).response
        }
    }
}
