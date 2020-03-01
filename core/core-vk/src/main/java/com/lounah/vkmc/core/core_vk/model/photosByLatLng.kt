package com.lounah.vkmc.core.core_vk.model

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class PhotosByLatLngResponse(
    val response: PhotosByLatLng
) {
    @Keep
    data class PhotosByLatLng(val items: List<PhotoByLatLng>)
}

@Keep
data class PhotoByLatLng(
    val id: String,
    val sizes: List<SearchPhotosSize>,
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

@Keep
data class SearchPhotosSize(
    val type: String,
    val url: String,
    val width: Int,
    val height: Int
)
