package com.lounah.vkmc.core.core_vk.model

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName


@Keep
class VKSaveInfoResponse(
    val response: List<VKSaveInfo>
)

@Keep
class VKSaveInfo(
    val id: Int,
    @SerializedName("album_id")
    val albumId: Int,
    @SerializedName("owner_id")
    val ownerId: Int
) {
    fun getAttachment() = "photo${ownerId}_$id"
}

@Keep
class VKFileUploadInfo(val server: String, val photo: String, val hash: String)

@Keep
class VKServerUploadInfoResponse(
    val response: VKServerUploadInfo
)

@Keep
class VKServerUploadInfo(
    @SerializedName("upload_url")
    val uploadUrl: String,
    @SerializedName("album_id")
    val albumId: Int,
    @SerializedName("user_id")
    val userId: Int
)
