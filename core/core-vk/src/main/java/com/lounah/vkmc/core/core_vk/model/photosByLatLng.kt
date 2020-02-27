package com.lounah.vkmc.core.core_vk.model

import com.google.gson.annotations.SerializedName

data class PhotosByLatLngResponse(
    val response: PhotosByLatLng
) {
    data class PhotosByLatLng(val items: List<PhotoByLatLng>)
}

data class PhotoByLatLng(
    val id: String,
    val sizes: List<PhotoSize>,
    @SerializedName("lat")
    val latitude: Double,
    @SerializedName("long")
    val longitude: Double
) {
    val photoMini: String
        get() = sizes.minBy { it.width * it.height }?.url.orEmpty()

    val photoLarge: String
        get() = sizes.maxBy { it.width * it.height }?.url.orEmpty()
}

data class PhotoSize(
    val type: String,
    val url: String,
    val width: Int,
    val height: Int
)
