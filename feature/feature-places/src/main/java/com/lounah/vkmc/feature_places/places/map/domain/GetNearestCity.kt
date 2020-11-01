package com.lounah.vkmc.feature_places.places.map.domain

import android.location.Location
import com.google.android.gms.maps.model.LatLng
import com.lounah.vkmc.core.core_vk.domain.City
import com.lounah.vkmc.core.core_vk.domain.GetCities

internal typealias CityId = String

class GetNearestCity(
    private val getCities: () -> List<City> = GetCities(),
    private val getDistance: (Location, Location) -> Int = GetDistance()
) : (LatLng) -> CityId {

    override fun invoke(latlng: LatLng): CityId {
        return getCities().minBy { city ->
            val start = Location("").apply {
                latitude = latlng.latitude
                longitude = latlng.longitude
            }
            val end = Location("").apply {
                latitude = city.latlng.first
                longitude = city.latlng.second
            }
            getDistance(start, end)
        }?.id.orEmpty()
    }
}

