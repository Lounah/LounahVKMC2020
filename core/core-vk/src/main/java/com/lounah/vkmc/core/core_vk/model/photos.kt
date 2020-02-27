package com.lounah.vkmc.core.core_vk.model

import com.google.gson.annotations.SerializedName

data class PhotosResponse(val response: Photos)

data class Photos(val items: List<Photo>)

data class Photo(
    val id: String,
    @SerializedName("album_id")
    val albumId: String,
    @SerializedName("owner_id")
    val ownerId: String,
    val sizes: List<PhotoSize>
) {
    val photo: String
        get() = sizes.maxBy { it.width * it.height }?.url.orEmpty()
}

data class PhotoSize(
    val url: String,
    val width: Int,
    val height: Int
)
