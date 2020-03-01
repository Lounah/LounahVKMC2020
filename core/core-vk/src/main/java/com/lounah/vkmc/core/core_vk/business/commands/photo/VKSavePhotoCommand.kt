package com.lounah.vkmc.core.core_vk.business.commands.photo

import com.google.gson.Gson
import com.lounah.vkmc.core.core_vk.business.VKApiCommandWrapper
import com.lounah.vkmc.core.core_vk.model.VKFileUploadInfo
import com.lounah.vkmc.core.core_vk.model.VKSaveInfo
import com.lounah.vkmc.core.core_vk.model.VKSaveInfoResponse
import com.vk.api.sdk.VKApiResponseParser

internal class VKSavePhotoCommandBuilder(
    private val method: String = "photos.save",
    private val responseParser: VKApiResponseParser<VKSaveInfo> = VKSavePhotoCommand.SaveInfoParser()
) : (VKFileUploadInfo) -> VKApiCommandWrapper<VKSaveInfo>() {

    override fun invoke(fileUploadInfo: VKFileUploadInfo): VKApiCommandWrapper<VKSaveInfo> {
        return VKSavePhotoCommand(
            fileUploadInfo,
            method,
            responseParser
        )
    }
}

internal class VKSavePhotoCommand(
    fileUploadInfo: VKFileUploadInfo,
    override val method: String = "photos.save",
    override val responseParser: VKApiResponseParser<VKSaveInfo> = SaveInfoParser()
) : VKApiCommandWrapper<VKSaveInfo>() {

    override val arguments: Map<String, String> = mapOf(
        "album_id" to fileUploadInfo.albumId.orEmpty(),
        "server" to fileUploadInfo.server,
        "photos_list" to fileUploadInfo.photos.orEmpty(),
        "hash" to fileUploadInfo.hash
    )

    class SaveInfoParser(private val gson: Gson = Gson()) : VKApiResponseParser<VKSaveInfo> {
        override fun parse(response: String): VKSaveInfo {
            return gson.fromJson(response, VKSaveInfoResponse::class.java).response.first()
        }
    }
}
