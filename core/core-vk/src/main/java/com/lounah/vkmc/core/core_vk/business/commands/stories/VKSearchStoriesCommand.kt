package com.lounah.vkmc.core.core_vk.business.commands.stories

import com.lounah.vkmc.core.core_vk.business.VKApiCommandWrapper
import com.lounah.vkmc.core.core_vk.business.gson
import com.lounah.vkmc.core.core_vk.model.StoriesByLatLngResponse
import com.lounah.vkmc.core.core_vk.model.StoryByLatLng
import com.vk.api.sdk.VKApiResponseParser

internal class VKSearchStoriesCommand(
    latLng: Pair<Double, Double>,
    override val method: String = "stories.search",
    override val responseParser: VKApiResponseParser<List<StoryByLatLng>> = VKGetStoriesResponseParser()
) : VKApiCommandWrapper<List<StoryByLatLng>>() {

    override val arguments: Map<String, String> = mapOf(
        "latitude" to "${latLng.first}",
        "q" to "*",
        "longitude" to "${latLng.second}",
        "radius" to "100000000",
        "count" to "100"
    )

    private class VKGetStoriesResponseParser : VKApiResponseParser<List<StoryByLatLng>> {
        override fun parse(response: String?): List<StoryByLatLng> {
            println("Parsing.. $response")
            return gson.fromJson(response, StoriesByLatLngResponse::class.java).response.items
        }
    }
}
