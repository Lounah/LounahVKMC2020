package com.lounah.vkmc.feature.feature_map.map.domain

import com.google.android.gms.maps.model.LatLng
import com.lounah.vkmc.core.core_vk.domain.market.CityId
import com.lounah.vkmc.core.core_vk.model.GroupWithAddress
import com.lounah.vkmc.feature.feature_map.map.ui.map.markers.MapMarker
import kotlin.random.Random

private val random = Random(1337)

class GroupsMapper : (List<GroupWithAddress>, CityId) -> List<MapMarker> {

    override fun invoke(groups: List<GroupWithAddress>, cityId: CityId): List<MapMarker> {
        return groups.map {
            MapMarker(
                latlng = LatLng(it.latitude + random.nextDouble(0.02), it.longitude + random.nextDouble(0.02)),
                cityId = cityId,
                id = it.id,
                address = it.address,
                isPhoto = false,
                photo = it.photo
            )
        }
    }
}
