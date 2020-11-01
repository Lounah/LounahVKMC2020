package com.lounah.vkmc.feature_places.places.map.config

import android.content.Context
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.maps.android.clustering.Cluster
import com.google.maps.android.clustering.ClusterManager
import com.google.maps.android.clustering.view.DefaultClusterRenderer
import com.lounah.vkmc.feature_places.places.map.config.markers.StoriesClusterItem

internal class MapMarkerRenderer(
    clusterManager: ClusterManager<StoriesClusterItem>,
    context: Context,
    map: GoogleMap,
    private val rendererDelegate: ClusterRenderer<StoriesClusterItem>
) : DefaultClusterRenderer<StoriesClusterItem>(context, map, clusterManager) {


    override fun onClusterItemRendered(clusterItem: StoriesClusterItem, marker: Marker) {
        super.onClusterItemRendered(clusterItem, marker)
        rendererDelegate.onClusterItemRendered(clusterItem, marker)
    }

    override fun onBeforeClusterItemRendered(item: StoriesClusterItem, markerOptions: MarkerOptions) {
        rendererDelegate.onBeforeClusterItemRendered(item, markerOptions)
    }

    override fun onClusterRendered(cluster: Cluster<StoriesClusterItem>, marker: Marker) {
        super.onClusterRendered(cluster, marker)
        rendererDelegate.onClusterRendered(cluster, marker)
    }

    override fun onBeforeClusterRendered(
        cluster: Cluster<StoriesClusterItem>,
        markerOptions: MarkerOptions
    ) {
        rendererDelegate.onBeforeClusterRendered(cluster, markerOptions)
    }

    override fun shouldRenderAsCluster(cluster: Cluster<StoriesClusterItem>): Boolean = cluster.size > 1
}
