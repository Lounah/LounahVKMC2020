package com.lounah.vkmc.feature.feature_map.map.domain

import com.google.android.gms.maps.model.LatLng
import com.lounah.vkmc.core.core_vk.domain.market.CityId
import com.lounah.vkmc.core.core_vk.model.PhotoByLatLng
import com.lounah.vkmc.feature.feature_map.map.ui.map.markers.MapMarker

class PhotosMapper : (List<PhotoByLatLng>, CityId) -> List<MapMarker> {

    override fun invoke(photos: List<PhotoByLatLng>, cityId: CityId): List<MapMarker> {
        return photos.map {
            MapMarker(
                latlng = LatLng(it.latitude, it.longitude),
                cityId = cityId,
                id = it.id,
                photo = it.photoMini,
                address = "",
                photoLarge = it.photoLarge,
                isPhoto = true
            )
        }
    }
}
