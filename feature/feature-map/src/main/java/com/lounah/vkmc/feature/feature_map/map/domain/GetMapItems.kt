package com.lounah.vkmc.feature.feature_map.map.domain

import com.lounah.vkmc.core.core_vk.domain.market.CityId
import com.lounah.vkmc.core.core_vk.model.GroupWithAddress
import com.lounah.vkmc.core.core_vk.model.PhotoByLatLng
import com.lounah.vkmc.feature.feature_map.map.presentation.RenderType
import com.lounah.vkmc.feature.feature_map.map.presentation.RenderType.*
import com.lounah.vkmc.feature.feature_map.map.ui.map.MapMarker
import io.reactivex.Observable
import io.reactivex.Single

class GetMapItems(
    private val getGroupsOrEvents: (CityId, Boolean) -> Single<List<GroupWithAddress>>,
    private val getPhotos: (CityId) -> Single<List<PhotoByLatLng>>,
    private val photosMapper: (List<PhotoByLatLng>, CityId) -> List<MapMarker>,
    private val groupsMapper: (List<GroupWithAddress>, CityId) -> List<MapMarker>
) : (CityId, RenderType) -> Observable<List<MapMarker>> {

    override fun invoke(cityId: CityId, renderType: RenderType): Observable<List<MapMarker>> {
        return when (renderType) {
            GROUPS -> getGroupsOrEvents(cityId, false).map { groupsMapper(it, cityId) }
            EVENTS -> getGroupsOrEvents(cityId, true).map { groupsMapper(it, cityId) }
            PHOTOS -> getPhotos(cityId).map { photosMapper(it, cityId) }
        }.toObservable()
    }
}
