package com.lounah.vkmc.core.core_vk.business.commands.wall

import com.google.gson.Gson
import com.lounah.vkmc.core.core_vk.business.VKApiCommandWrapper
import com.lounah.vkmc.core.core_vk.business.commands.wall.VKSaveWallPhotoCommand.SaveInfoParser
import com.lounah.vkmc.core.core_vk.model.VKFileUploadInfo
import com.lounah.vkmc.core.core_vk.model.VKSaveInfo
import com.lounah.vkmc.core.core_vk.model.VKSaveInfoResponse
import com.vk.api.sdk.VKApiResponseParser

internal class VKSaveWallPhotoCommandBuilder(
    private val method: String = "photos.saveWallPhoto",
    private val responseParser: VKApiResponseParser<VKSaveInfo> = SaveInfoParser()
) : (VKFileUploadInfo) -> VKApiCommandWrapper<VKSaveInfo>() {

    override fun invoke(fileUploadInfo: VKFileUploadInfo): VKApiCommandWrapper<VKSaveInfo> {
        return VKSaveWallPhotoCommand(
            fileUploadInfo,
            method,
            responseParser
        )
    }
}

internal class VKSaveWallPhotoCommand(
    fileUploadInfo: VKFileUploadInfo,
    override val method: String = "photos.saveWallPhoto",
    override val responseParser: VKApiResponseParser<VKSaveInfo> = SaveInfoParser()
) : VKApiCommandWrapper<VKSaveInfo>() {

    override val arguments: Map<String, String> = mapOf(
        "server" to fileUploadInfo.server,
        "photo" to fileUploadInfo.photo,
        "hash" to fileUploadInfo.hash
    )

    class SaveInfoParser(private val gson: Gson = Gson()) : VKApiResponseParser<VKSaveInfo> {
        override fun parse(response: String): VKSaveInfo {
            return gson.fromJson(response, VKSaveInfoResponse::class.java).response.first()
        }
    }
}
