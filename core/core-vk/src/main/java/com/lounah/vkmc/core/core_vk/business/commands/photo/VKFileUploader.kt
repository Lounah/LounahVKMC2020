package com.lounah.vkmc.core.core_vk.business.commands.photo

import android.net.Uri
import com.google.gson.Gson
import com.lounah.vkmc.core.core_vk.model.VKFileUploadInfo
import com.lounah.vkmc.core.core_vk.model.VKServerUploadInfo
import com.vk.api.sdk.VKApiManager
import com.vk.api.sdk.VKApiResponseParser
import com.vk.api.sdk.VKHttpPostCall
import com.vk.api.sdk.internal.ApiCommand

private const val RETRY_COUNT = 3

internal class VKFileUploader(
    private val serverUploadInfo: VKServerUploadInfo,
    private val uri: Uri
) : ApiCommand<VKFileUploadInfo>() {

    override fun onExecute(manager: VKApiManager): VKFileUploadInfo {
        val fileUploadCall = VKHttpPostCall.Builder()
            .url(serverUploadInfo.uploadUrl)
            .args("photo", uri, "stub.jpg")
            .retryCount(RETRY_COUNT)
            .build()

        return manager.execute(fileUploadCall, null, FileUploadInfoParser())
    }

    private class FileUploadInfoParser(
        private val gson: Gson = Gson()
    ) : VKApiResponseParser<VKFileUploadInfo> {
        override fun parse(response: String): VKFileUploadInfo =
            gson.fromJson<VKFileUploadInfo>(response, VKFileUploadInfo::class.java)
    }
}

internal class VKFileUploaderBuilder : (VKServerUploadInfo, Uri) -> ApiCommand<VKFileUploadInfo>() {

    override fun invoke(
        serverUploadInfo: VKServerUploadInfo,
        uri: Uri
    ): ApiCommand<VKFileUploadInfo> = VKFileUploader(serverUploadInfo, uri)
}
