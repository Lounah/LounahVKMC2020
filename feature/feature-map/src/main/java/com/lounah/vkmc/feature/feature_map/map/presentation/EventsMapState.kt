package com.lounah.vkmc.feature.feature_map.map.presentation

import com.lounah.vkmc.core.core_vk.domain.market.CityId
import com.lounah.vkmc.feature.feature_map.map.presentation.EventsMapAction.*
import com.lounah.vkmc.feature.feature_map.map.presentation.RenderType.EVENTS
import com.lounah.vkmc.feature.feature_map.map.ui.map.MapMarker

enum class RenderType {
    EVENTS, GROUPS, PHOTOS
}

data class EventsMapState(
    val renderType: RenderType = EVENTS,
    val items: List<MapMarker> = emptyList(),
    val cityId: CityId = "0",
    val isLoading: Boolean = false,
    val hasError: Boolean = false
)

fun EventsMapState.reduce(action: EventsMapAction): EventsMapState {
    return when (action) {
        is StartLoading -> copy(isLoading = true, hasError = false)
        is ErrorLoading -> copy(isLoading = false, hasError = true)
        is OnRenderTypeChanged -> copy(renderType = action.renderType, items = emptyList(), hasError = false)
        is OnCityIdChanged -> copy(cityId = action.cityId)
        is OnMapItemsLoaded -> {
            val cityId = action.items.firstOrNull()?.cityId ?: cityId
            copy(items = action.items, cityId = cityId, isLoading = false, hasError = false)
        }
        else -> this
    }
}
