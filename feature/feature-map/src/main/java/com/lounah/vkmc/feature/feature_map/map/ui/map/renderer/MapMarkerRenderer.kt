package com.lounah.vkmc.feature.feature_map.map.ui.map.renderer

import android.content.Context
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.maps.android.clustering.Cluster
import com.google.maps.android.clustering.ClusterManager
import com.google.maps.android.clustering.view.DefaultClusterRenderer
import com.lounah.vkmc.feature.feature_map.map.ui.map.markers.MapMarker

class MapMarkerRenderer(
    clusterManager: ClusterManager<MapMarker>,
    context: Context,
    map: GoogleMap,
    private val groupsRendererDelegate: ClusterRendererDelegate,
    private val photosRendererDelegate: ClusterRendererDelegate
) : DefaultClusterRenderer<MapMarker>(context, map, clusterManager) {


    override fun onClusterItemRendered(clusterItem: MapMarker, marker: Marker) {
        super.onClusterItemRendered(clusterItem, marker)
        if (clusterItem.isPhoto) {
            photosRendererDelegate.onClusterItemRendered(clusterItem, marker)
        } else {
            groupsRendererDelegate.onClusterItemRendered(clusterItem, marker)
        }
    }

    override fun onBeforeClusterItemRendered(item: MapMarker, markerOptions: MarkerOptions) {
        if (item.isPhoto) {
            photosRendererDelegate.onBeforeClusterItemRendered(item, markerOptions)
        } else {
            groupsRendererDelegate.onBeforeClusterItemRendered(item, markerOptions)
        }
    }

    override fun onClusterRendered(cluster: Cluster<MapMarker>, marker: Marker) {
        super.onClusterRendered(cluster, marker)
        if (cluster.items.any { it.isPhoto }) {
            photosRendererDelegate.onClusterRendered(cluster, marker)
        } else {
            groupsRendererDelegate.onClusterRendered(cluster, marker)
        }
    }

    override fun onBeforeClusterRendered(
        cluster: Cluster<MapMarker>,
        markerOptions: MarkerOptions
    ) {
        if (cluster.items.any { it.isPhoto }) {
            photosRendererDelegate.onBeforeClusterRendered(cluster, markerOptions)
        } else {
            groupsRendererDelegate.onBeforeClusterRendered(cluster, markerOptions)
        }
    }

    override fun shouldRenderAsCluster(cluster: Cluster<MapMarker>): Boolean = cluster.size > 1
}
