package com.lounah.vkmc.core.core_vk.model

data class FaveProductsResponse(
    val response: FaveProducts
) {

    data class FaveProducts(val items: List<FaveItem>)
}

data class FaveItem(val product: FaveProduct)

data class FaveProduct(
    val id: String
)
