package com.lounah.vkmc.core.core_vk.business.commands.photo

import android.net.Uri
import com.lounah.vkmc.core.core_vk.business.VKApiCommandWrapper
import com.lounah.vkmc.core.core_vk.domain.AlbumId
import com.lounah.vkmc.core.core_vk.model.VKFileUploadInfo
import com.lounah.vkmc.core.core_vk.model.VKSaveInfo
import com.lounah.vkmc.core.core_vk.model.VKServerUploadInfo
import com.vk.api.sdk.VKApiManager
import com.vk.api.sdk.internal.ApiCommand

internal class VKAlbumPhotoUploader(
    albumId: AlbumId,
    private val vkSavePhotoCommandBuilder: (VKFileUploadInfo) -> VKApiCommandWrapper<VKSaveInfo> = VKSavePhotoCommandBuilder(),
    private val vkFileUploaderBuilder: (VKServerUploadInfo, Uri) -> ApiCommand<VKFileUploadInfo> = VKFileUploaderBuilder(),
    private val serverUploadInfoCommand: VKApiCommandWrapper<VKServerUploadInfo> = VKServerPhotoUploadInfoCommand(albumId)
) : (Uri, VKApiManager) -> String {

    override fun invoke(uri: Uri, manager: VKApiManager): String {
        val serverUploadInfo = serverUploadInfoCommand.execute(manager)
        val vkFileUploader = vkFileUploaderBuilder(serverUploadInfo, uri)
        val fileUploadInfo = vkFileUploader.execute(manager)

        return vkSavePhotoCommandBuilder(fileUploadInfo).execute(manager).id.toString()
    }
}

internal class UploadPhotoApiCommand(
    private val albumId: AlbumId,
    private val uri: Uri,
    private val photoUploader: (Uri, VKApiManager) -> String = VKAlbumPhotoUploader(albumId)
) : ApiCommand<String>() {

    override fun onExecute(manager: VKApiManager): String {
        return photoUploader(uri, manager)
    }
}