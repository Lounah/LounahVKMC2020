package com.lounah.vkmc.feature_places.places.map.presentation

import com.google.android.gms.maps.model.LatLng
import com.lounah.vkmc.feature_places.places.map.config.markers.StoriesClusterItem

sealed class PlacesEvent {
    class LoadStories(val location: LatLng, val force: Boolean = false): PlacesEvent()
    class LoadingFailed(val error: Throwable) : PlacesEvent()
    class StoriesLoaded(val places: List<StoriesClusterItem>) : PlacesEvent()
    object HideLoading : PlacesEvent()
}
