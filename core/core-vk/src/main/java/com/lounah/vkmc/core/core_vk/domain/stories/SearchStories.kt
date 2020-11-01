package com.lounah.vkmc.core.core_vk.domain.stories

import com.lounah.vkmc.core.core_vk.business.commands.stories.VKSearchStoriesCommand
import com.lounah.vkmc.core.core_vk.domain.City
import com.lounah.vkmc.core.core_vk.domain.GetCitiesSingle
import com.lounah.vkmc.core.core_vk.domain.market.CityId
import com.lounah.vkmc.core.core_vk.model.StoryByLatLng
import com.vk.api.sdk.VK
import io.reactivex.Single

class SearchStories(
    private val getCities: () -> Single<List<City>> = GetCitiesSingle()
) : (CityId) -> Single<List<StoryByLatLng>> {

    override fun invoke(cityId: CityId): Single<List<StoryByLatLng>> {
        return getCities().map { it.first { city ->  city.id == cityId } }
            .map { VK.executeSync(VKSearchStoriesCommand(it.latlng)) }
    }
}
