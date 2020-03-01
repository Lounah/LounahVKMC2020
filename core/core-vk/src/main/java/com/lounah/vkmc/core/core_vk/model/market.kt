package com.lounah.vkmc.core.core_vk.model

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class MarketResponse(
    val response: Markets
) {

    @Keep
    data class Markets(val items: List<Market>)
}

@Keep
data class Market(
    val id: String,
    val name: String,
    @SerializedName("is_closed")
    val isClosed: Int,
    @SerializedName("photo_100")
    val photo: String
) {
    val closed: Boolean
        get() = isClosed == 1
}

@Keep
data class ProductsResponse(
    val response: Products
) {
    @Keep
    data class Products(val items: List<Product>)
}

@Keep
data class Product(
    val id: String,
    @SerializedName("owner_id")
    val ownerId: String,
    val availability: Int,
    val description: String,
    val price: Price,
    @SerializedName("thumb_photo")
    val photo: String,
    val title: String,
    val url: String
) {

    @Keep
    data class Price(val currency: Currency,
                     val amount: String) {
        val harmonizedAmount: String
            get() = "${amount.toLong() / 100}"
    }

    @Keep
    data class Currency(val name: String) {
        val isRub: Boolean
            get() = name == "RUB"
        val isUsd: Boolean
            get() = name == "USD"
        val isEur: Boolean
            get() = name == "EUR"
    }
}
