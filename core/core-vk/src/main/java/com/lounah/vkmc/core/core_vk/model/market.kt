package com.lounah.vkmc.core.core_vk.model

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class MarketReponse(
    val response: Markets
) {

    data class Markets(val items: List<Market>)
}

data class Market(
    val id: String,
    val name: String,
    @SerializedName("photo_100")
    val photo: String
)
