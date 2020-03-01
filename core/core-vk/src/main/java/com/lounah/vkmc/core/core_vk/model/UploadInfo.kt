package com.lounah.vkmc.core.core_vk.model

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName


@Keep
data class VKSaveInfoResponse(
    val response: List<VKSaveInfo>
)

@Keep
data class VKSaveInfo(
    val id: Int,
    @SerializedName("album_id")
    val albumId: Int,
    @SerializedName("owner_id")
    val ownerId: Int
) {
    fun getAttachment() = "photo${ownerId}_$id"
}

@Keep
data class VKFileUploadInfo(
    @SerializedName("aid")
    val albumId: String? = "",
    val server: String,
    val photo: String? = "",
    @SerializedName("photos_list")
    val photos: String? = "",
    val hash: String
)

@Keep
data class VKServerUploadInfoResponse(
    val response: VKServerUploadInfo
)

@Keep
data class VKServerUploadInfo(
    @SerializedName("upload_url")
    val uploadUrl: String,
    @SerializedName("album_id")
    val albumId: Int,
    @SerializedName("user_id")
    val userId: Int
)
