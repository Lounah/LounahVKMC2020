package com.lounah.vkmc.core.core_vk.model

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class StoriesByLatLngResponse(
    val response: StoriesByLatLng
) {
    @Keep
    data class StoriesByLatLng(val items: List<StoryByLatLng>)
}

@Keep
data class StoryByLatLng(
    val id: String,
    @SerializedName("lat")
    val latitude: Double,
    @SerializedName("long")
    val longitude: Double
) {
//    val photoMini: String
//        get() = sizes.minBy { it.width * it.height }?.url.orEmpty()
//
//    val photoLarge: String
//        get() = sizes.maxBy { it.width * it.height }?.url.orEmpty()
}
