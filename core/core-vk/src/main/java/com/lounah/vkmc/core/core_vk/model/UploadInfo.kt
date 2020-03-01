package com.lounah.vkmc.core.core_vk.model

import com.google.gson.annotations.SerializedName


data class VKSaveInfoResponse(
    val response: List<VKSaveInfo>
)

data class VKSaveInfo(
    val id: Int,
    @SerializedName("album_id")
    val albumId: Int,
    @SerializedName("owner_id")
    val ownerId: Int
) {
    fun getAttachment() = "photo${ownerId}_$id"
}

data class VKFileUploadInfo(
    @SerializedName("aid")
    val albumId: String? = "",
    val server: String,
    val photo: String? = "",
    @SerializedName("photos_list")
    val photos: String? = "",
    val hash: String
)

data class VKServerUploadInfoResponse(
    val response: VKServerUploadInfo
)

data class VKServerUploadInfo(
    @SerializedName("upload_url")
    val uploadUrl: String,
    @SerializedName("album_id")
    val albumId: Int,
    @SerializedName("user_id")
    val userId: Int
)
