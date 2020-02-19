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

@Keep
data class ProductsResponse(
    val response: Products
) {
    data class Products(val items: List<Product>)
}

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

    data class Price(val currency: Currency,
                     val amount: String) {
        val formattedAmount: String
            get() = "${amount.toLong() / 100}"
    }

    data class Currency(val name: String) {
        val isRub: Boolean
            get() = name == "RUB"
        val isUsd: Boolean
            get() = name == "USD"
        val isEur: Boolean
            get() = name == "EUR"
    }
}
