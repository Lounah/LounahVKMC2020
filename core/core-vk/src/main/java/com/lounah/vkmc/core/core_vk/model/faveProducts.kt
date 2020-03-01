package com.lounah.vkmc.core.core_vk.model

import androidx.annotation.Keep

@Keep
data class FaveProductsResponse(
    val response: FaveProducts
) {

    @Keep
    data class FaveProducts(val items: List<FaveItem>)
}

@Keep
data class FaveItem(val product: FaveProduct)

@Keep
data class FaveProduct(
    val id: String
)
