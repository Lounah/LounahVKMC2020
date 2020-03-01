package com.lounah.vkmc.feature.feature_map.map.ui.map.renderer

import android.content.Context
import android.view.ViewGroup
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.maps.android.clustering.Cluster
import com.google.maps.android.ui.IconGenerator
import com.lounah.vkmc.core.extensions.dp
import com.lounah.vkmc.feature.feature_map.R
import com.lounah.vkmc.feature.feature_map.map.ui.map.IconCache
import com.lounah.vkmc.feature.feature_map.map.ui.map.markers.GroupOrEventView
import com.lounah.vkmc.feature.feature_map.map.ui.map.markers.MapMarker

internal class GroupsClusterRenderer(
    private val iconCache: IconCache,
    context: Context
) : ClusterRendererDelegate {

    private val groupIconSizeDp = 32.dp(context)

    private val singleGroupIcon = GroupOrEventView(context).apply {
        layoutParams = ViewGroup.LayoutParams(groupIconSizeDp, groupIconSizeDp)
    }
    private val clusterGroupIcon = GroupOrEventView(context).apply {
        layoutParams = ViewGroup.LayoutParams(groupIconSizeDp, groupIconSizeDp)
    }
    private val groupIconGenerator = IconGenerator(context).apply {
        setBackground(null)
        setContentView(singleGroupIcon)
    }
    private val clusterGroupIconGenerator = IconGenerator(context).apply {
        setBackground(null)
        setContentView(clusterGroupIcon)
    }

    override fun onClusterItemRendered(clusterItem: MapMarker, marker: Marker) {
        iconCache.get(clusterItem.photo)?.let(singleGroupIcon::setImage)
            ?: singleGroupIcon.setIcon(clusterItem.photo)
        singleGroupIcon.setBackground(R.drawable.bg_cluster_gray)
        val icon = groupIconGenerator.makeIcon()
        marker.setIcon(BitmapDescriptorFactory.fromBitmap(icon))
    }

    override fun onBeforeClusterItemRendered(item: MapMarker, markerOptions: MarkerOptions) {
        iconCache.put(item.photo, false)
        iconCache.get(item.photo)?.let(singleGroupIcon::setImage)
            ?: singleGroupIcon.setIcon(item.photo)
        singleGroupIcon.setBackground(R.drawable.bg_cluster_gray)
        val icon = groupIconGenerator.makeIcon()
        markerOptions.icon(BitmapDescriptorFactory.fromBitmap(icon))
    }

    override fun onClusterRendered(cluster: Cluster<MapMarker>, marker: Marker) {
        clusterGroupIcon.setCounter(cluster.size)
        val icon = clusterGroupIconGenerator.makeIcon()
        marker.setIcon(BitmapDescriptorFactory.fromBitmap(icon))
    }

    override fun onBeforeClusterRendered(
        cluster: Cluster<MapMarker>,
        markerOptions: MarkerOptions
    ) {
        iconCache.put(cluster.items.first().photo, false)
        clusterGroupIcon.setCounter(cluster.size)
        clusterGroupIcon.setBackground(R.drawable.bg_cluster_gray)
        val icon = clusterGroupIconGenerator.makeIcon()
        markerOptions.icon(BitmapDescriptorFactory.fromBitmap(icon))
    }
}
