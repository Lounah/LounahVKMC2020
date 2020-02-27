package com.lounah.vkmc.feature.feature_map.map.ui.map

import android.content.Context
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.clustering.ClusterItem
import com.google.maps.android.clustering.ClusterManager
import com.lounah.vkmc.core.core_vk.domain.market.CityId

class MapMarker(
    val id: String,
    val cityId: CityId,
    val latlng: LatLng,
    val photo: String,
    val address: String,
    val isPhoto: Boolean,
    val photoLarge: String = ""
) : ClusterItem {

    override fun getPosition(): LatLng = latlng

    override fun getSnippet(): String = ""

    override fun getTitle(): String = ""
}

class EventsMapClusterManager(
    context: Context,
    map: GoogleMap,
    private val onCameraIdleCallback: () -> Unit = {}
) : ClusterManager<MapMarker>(context, map) {

    override fun onCameraIdle() {
        super.onCameraIdle()
        onCameraIdleCallback()
    }
}
