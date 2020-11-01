package com.lounah.vkmc.feature_places.places.map.config

import android.content.Context
import com.google.android.gms.maps.GoogleMap
import com.google.maps.android.clustering.ClusterManager
import com.lounah.vkmc.feature_places.places.map.config.markers.StoriesClusterItem

internal class StoriesMapClusterManager(
    context: Context,
    map: GoogleMap,
    private val onCameraIdleCallback: () -> Unit = {}
) : ClusterManager<StoriesClusterItem>(context, map) {

    override fun onCameraIdle() {
        super.onCameraIdle()
        onCameraIdleCallback()
    }
}
