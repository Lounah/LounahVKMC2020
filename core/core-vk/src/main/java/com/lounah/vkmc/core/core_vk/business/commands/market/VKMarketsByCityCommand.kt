package com.lounah.vkmc.core.core_vk.business.commands.market

import com.lounah.vkmc.core.core_vk.business.VKApiCommandWrapper
import com.lounah.vkmc.core.core_vk.business.gson
import com.lounah.vkmc.core.core_vk.model.Market
import com.lounah.vkmc.core.core_vk.model.MarketResponse
import com.vk.api.sdk.VKApiResponseParser

internal class VKMarketsByCityCommand(
    cityId: String,
    offset: Int,
    override val method: String = "groups.search",
    override val responseParser: VKApiResponseParser<List<Market>> = VKMarketByCityResponseParser()
) : VKApiCommandWrapper<List<Market>>() {

    override val arguments: Map<String, String> =
        mapOf("city_id" to cityId, "q" to "*", "market" to "1", "count" to "50", "offset" to "$offset")

    private class VKMarketByCityResponseParser : VKApiResponseParser<List<Market>> {
        override fun parse(response: String): List<Market> {
            return gson.fromJson(response, MarketResponse::class.java)
                .response.items.filterNot(Market::closed)
        }
    }
}
