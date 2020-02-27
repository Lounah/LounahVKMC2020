package com.lounah.vkmc.core.core_vk.business.commands.market

import com.lounah.vkmc.core.core_vk.business.VKApiCommandWrapper
import com.lounah.vkmc.core.core_vk.business.gson
import com.lounah.vkmc.core.core_vk.model.Product
import com.lounah.vkmc.core.core_vk.model.ProductsResponse
import com.vk.api.sdk.VKApiResponseParser

internal class VKProductsByMarketCommand(
    marketId: String,
    offset: Int,
    override val method: String = "market.get",
    override val responseParser: VKApiResponseParser<List<Product>> = VKProductsByMarketParser()
) : VKApiCommandWrapper<List<Product>>() {

    override val arguments: Map<String, String> =
        mapOf("owner_id" to "-$marketId", "count" to "50", "offset" to "$offset")

    private class VKProductsByMarketParser : VKApiResponseParser<List<Product>> {
        override fun parse(response: String): List<Product> {
            return gson.fromJson(response, ProductsResponse::class.java).response.items
        }
    }
}
