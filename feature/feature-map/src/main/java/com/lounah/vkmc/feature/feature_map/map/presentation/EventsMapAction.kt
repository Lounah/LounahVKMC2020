package com.lounah.vkmc.feature.feature_map.map.presentation

import com.google.android.gms.maps.model.LatLng
import com.lounah.vkmc.core.core_vk.domain.market.CityId
import com.lounah.vkmc.feature.feature_map.map.ui.map.MapMarker

sealed class EventsMapAction {
    object StartLoading : EventsMapAction()
    object ErrorLoading : EventsMapAction()
    class OnCityIdChanged(val cityId: CityId) : EventsMapAction()
    class OnMapIdle(val cameraLatLng: LatLng) : EventsMapAction()
    class OnRetryLoading(val cameraLatLng: LatLng) : EventsMapAction()
    class OnMapItemsLoaded(val items: List<MapMarker>) : EventsMapAction()
    class OnRenderTypeChanged(val renderType: RenderType) : EventsMapAction()
    class OnMapItemClicked(val item: MapMarker) : EventsMapAction()
}
