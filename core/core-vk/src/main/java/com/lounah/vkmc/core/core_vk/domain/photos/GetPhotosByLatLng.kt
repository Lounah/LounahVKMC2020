package com.lounah.vkmc.core.core_vk.domain.photos

import com.lounah.vkmc.core.core_vk.business.commands.photo.VKSearchPhotosCommand
import com.lounah.vkmc.core.core_vk.domain.City
import com.lounah.vkmc.core.core_vk.domain.GetCities
import com.lounah.vkmc.core.core_vk.domain.market.CityId
import com.lounah.vkmc.core.core_vk.model.PhotoByLatLng
import com.vk.api.sdk.VK
import io.reactivex.Single

class GetPhotosByLatLng(
    private val getCities: () -> Single<List<City>> = GetCities()
) : (CityId) -> Single<List<PhotoByLatLng>> {

    override fun invoke(cityId: CityId): Single<List<PhotoByLatLng>> {
        return getCities().map { it.first { city ->  city.id == cityId } }
            .map { VK.executeSync(VKSearchPhotosCommand(it.latlng)) }
    }
}
