package com.lounah.vkmc.feature.feature_map.map.ui.map.renderer

import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.maps.android.clustering.Cluster
import com.lounah.vkmc.feature.feature_map.map.ui.map.markers.MapMarker

interface ClusterRendererDelegate {
    fun onClusterItemRendered(clusterItem: MapMarker, marker: Marker)
    fun onBeforeClusterItemRendered(item: MapMarker, markerOptions: MarkerOptions)
    fun onClusterRendered(cluster: Cluster<MapMarker>, marker: Marker)
    fun onBeforeClusterRendered(cluster: Cluster<MapMarker>, markerOptions: MarkerOptions)
}
