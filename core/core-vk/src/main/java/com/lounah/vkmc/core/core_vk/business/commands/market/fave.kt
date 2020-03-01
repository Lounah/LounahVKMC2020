package com.lounah.vkmc.core.core_vk.business.commands.market

import com.lounah.vkmc.core.core_vk.business.VKApiCommandWrapper
import com.lounah.vkmc.core.core_vk.business.gson
import com.lounah.vkmc.core.core_vk.model.FaveItem
import com.lounah.vkmc.core.core_vk.model.FaveProduct
import com.lounah.vkmc.core.core_vk.model.FaveProductsResponse
import com.vk.api.sdk.VKApiResponseParser

internal class VKAddProductToFaveCommand(
    ownerId: String,
    productId: String,
    override val method: String = "fave.addProduct",
    override val arguments: Map<String, String> = mapOf(
        "owner_id" to "-$ownerId",
        "id" to productId
    ),
    override val responseParser: VKApiResponseParser<Unit> = VKAddProductToFaveCommandResponseParser()
) : VKApiCommandWrapper<Unit>() {

    private class VKAddProductToFaveCommandResponseParser : VKApiResponseParser<Unit> {
        override fun parse(response: String?) = Unit
    }
}

internal class VKRemoveProductFromFaveCommand(
    ownerId: String,
    productId: String,
    override val method: String = "fave.removeProduct",
    override val arguments: Map<String, String> = mapOf(
        "owner_id" to "-$ownerId",
        "id" to productId
    ),
    override val responseParser: VKApiResponseParser<Unit> = VKRemoveProductFromFaveCommandResponseParser()
) : VKApiCommandWrapper<Unit>() {

    private class VKRemoveProductFromFaveCommandResponseParser : VKApiResponseParser<Unit> {
        override fun parse(response: String?) = Unit
    }
}

internal class VKGetFaveProducts(
    override val method: String = "fave.getPages",
    override val arguments: Map<String, String> = mapOf("item_type" to "product", "count" to "100"),
    override val responseParser: VKApiResponseParser<List<FaveProduct>> = VKGetFaveProductsResponseParser()
) : VKApiCommandWrapper<List<FaveProduct>>() {

    private class VKGetFaveProductsResponseParser : VKApiResponseParser<List<FaveProduct>> {

        override fun parse(response: String?): List<FaveProduct> {
            return gson.fromJson(response, FaveProductsResponse::class.java)
                .response
                .items
                .map(FaveItem::product)
        }
    }
}
