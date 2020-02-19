package com.lounah.vkmc.core.core_vk.business.commands.market

import com.google.gson.Gson
import com.lounah.vkmc.core.core_vk.business.VKApiCommandWrapper
import com.lounah.vkmc.core.core_vk.model.Market
import com.lounah.vkmc.core.core_vk.model.MarketReponse
import com.vk.api.sdk.VKApiResponseParser

internal class VKMarketsByCityCommand(
    cityId: String,
    offset: Int,
    override val method: String = "groups.search",
    override val responseParser: VKApiResponseParser<List<Market>> = VKMarketByCityResponseParser()
) : VKApiCommandWrapper<List<Market>>() {

    override val arguments: Map<String, String> =
        mapOf("city_id" to cityId, "q" to "Adidas", "market" to "1", "count" to "50", "offset" to "$offset")

    private class VKMarketByCityResponseParser(
        private val gson: Gson = Gson()
    ) : VKApiResponseParser<List<Market>> {
        override fun parse(response: String): List<Market> {
            return gson.fromJson(response, MarketReponse::class.java).response.items
        }
    }
}
