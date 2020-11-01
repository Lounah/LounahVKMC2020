package com.lounah.vkmc.feature.feature_map.map.business

import android.location.Location
import com.google.android.gms.maps.model.LatLng
import com.lounah.vkmc.core.core_vk.domain.City
import com.lounah.vkmc.core.core_vk.domain.GetCitiesSingle
import com.lounah.vkmc.core.core_vk.domain.market.CityId
import io.reactivex.Observable
import io.reactivex.Single

class GetNearestCity(
    private val getCities: () -> Single<List<City>> = GetCitiesSingle(),
    private val getDistance: (Location, Location) -> Int = GetDistance()
) : (LatLng) -> Observable<CityId> {

    override fun invoke(latlng: LatLng): Observable<CityId> {
        return getCities().map { cities ->
            cities.minBy {
                val start = Location("").apply {
                    latitude = latlng.latitude
                    longitude = latlng.longitude
                }
                val end = Location("").apply {
                    latitude = it.latlng.first
                    longitude = it.latlng.second
                }
                getDistance(start, end)
            }?.id.orEmpty()
        }.toObservable()
    }
}
