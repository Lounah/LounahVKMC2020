package com.lounah.vkmc.feature_places.places.map.config

import android.content.Context
import android.view.ViewGroup
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.maps.android.clustering.Cluster
import com.google.maps.android.ui.IconGenerator
import com.lounah.vkmc.core.extensions.dp
import com.lounah.vkmc.feature_places.places.map.config.markers.StoriesClusterItem
import com.lounah.vkmc.feature_places.places.map.config.markers.StoryMarkerView

internal class StoriesClusterRenderer(
    context: Context
) : ClusterRenderer<StoriesClusterItem> {

    private val iconSizeDp = 48.dp(context)

    private val singlePhotoIcon = StoryMarkerView(context).apply {
        layoutParams = ViewGroup.LayoutParams(iconSizeDp, iconSizeDp)
    }
    private val clusterPhotoIcon = StoryMarkerView(context).apply {
        layoutParams = ViewGroup.LayoutParams(iconSizeDp, iconSizeDp)
    }
    private val photoIconGenerator = IconGenerator(context).apply {
        setBackground(null)
        setContentView(singlePhotoIcon)
    }
    private val clusterPhotoIconGenerator = IconGenerator(context).apply {
        setBackground(null)
        setContentView(clusterPhotoIcon)
    }

    override fun onClusterItemRendered(clusterItem: StoriesClusterItem, marker: Marker) {
        val icon = photoIconGenerator.makeIcon()
        marker.setIcon(BitmapDescriptorFactory.fromBitmap(icon))
    }

    override fun onBeforeClusterItemRendered(item: StoriesClusterItem, markerOptions: MarkerOptions) {
        val icon = photoIconGenerator.makeIcon()
        markerOptions.icon(BitmapDescriptorFactory.fromBitmap(icon))
    }

    override fun onClusterRendered(cluster: Cluster<StoriesClusterItem>, marker: Marker) {
        clusterPhotoIcon.setCounter(cluster.size)
        val icon = clusterPhotoIconGenerator.makeIcon()
        marker.setIcon(BitmapDescriptorFactory.fromBitmap(icon))
    }

    override fun onBeforeClusterRendered(
        cluster: Cluster<StoriesClusterItem>,
        markerOptions: MarkerOptions
    ) {
        clusterPhotoIcon.setCounter(cluster.size)
        val icon = clusterPhotoIconGenerator.makeIcon()
        markerOptions.icon(BitmapDescriptorFactory.fromBitmap(icon))
    }
}
