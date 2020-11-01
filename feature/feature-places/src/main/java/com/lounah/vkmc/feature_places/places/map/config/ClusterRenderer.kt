package com.lounah.vkmc.feature_places.places.map.config

import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.maps.android.clustering.Cluster
import com.google.maps.android.clustering.ClusterItem

internal interface ClusterRenderer<T : ClusterItem> {
    fun onClusterItemRendered(clusterItem: T, marker: Marker)
    fun onBeforeClusterItemRendered(item: T, markerOptions: MarkerOptions)
    fun onClusterRendered(cluster: Cluster<T>, marker: Marker)
    fun onBeforeClusterRendered(cluster: Cluster<T>, markerOptions: MarkerOptions)
}
