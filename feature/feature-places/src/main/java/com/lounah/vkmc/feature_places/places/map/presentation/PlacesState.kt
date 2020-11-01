package com.lounah.vkmc.feature_places.places.map.presentation

import com.lounah.vkmc.feature_places.places.map.config.markers.StoriesClusterItem
import com.lounah.vkmc.feature_places.places.map.domain.CityId

internal data class PlacesState(
    val loading: Boolean = false,
    val currentCityId: CityId = "",
    val stories: List<StoriesClusterItem> = emptyList()
)
