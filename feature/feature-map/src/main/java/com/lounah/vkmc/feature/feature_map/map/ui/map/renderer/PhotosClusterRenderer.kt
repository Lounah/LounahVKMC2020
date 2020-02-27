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
import com.lounah.vkmc.feature.feature_map.map.ui.map.markers.MapMarker
import com.lounah.vkmc.feature.feature_map.map.ui.map.markers.PhotoMarkerView

internal class PhotosClusterRenderer(
    private val iconCache: IconCache,
    context: Context
) : ClusterRendererDelegate {

    private val photoIconSizeDp = 58.dp(context)

    private val singlePhotoIcon = PhotoMarkerView(context).apply {
        layoutParams = ViewGroup.LayoutParams(photoIconSizeDp, photoIconSizeDp)
    }
    private val clusterPhotoIcon = PhotoMarkerView(context).apply {
        layoutParams = ViewGroup.LayoutParams(photoIconSizeDp, photoIconSizeDp)
    }
    private val photoIconGenerator = IconGenerator(context).apply {
        setBackground(null)
        setContentView(singlePhotoIcon)
    }
    private val clusterPhotoIconGenerator = IconGenerator(context).apply {
        setBackground(null)
        setContentView(clusterPhotoIcon)
    }

    override fun onClusterItemRendered(clusterItem: MapMarker, marker: Marker) {
        iconCache.get(clusterItem.photo)?.let(singlePhotoIcon::setImage)
            ?: singlePhotoIcon.setIcon(clusterItem.photo)
        val icon = photoIconGenerator.makeIcon()
        marker.setIcon(BitmapDescriptorFactory.fromBitmap(icon))
    }

    override fun onBeforeClusterItemRendered(item: MapMarker, markerOptions: MarkerOptions) {
        iconCache.put(item.photo, true)
        iconCache.get(item.photo)?.let(singlePhotoIcon::setImage)
            ?: singlePhotoIcon.setIcon(item.photo)
        val icon = photoIconGenerator.makeIcon()
        markerOptions.icon(BitmapDescriptorFactory.fromBitmap(icon))
    }

    override fun onClusterRendered(cluster: Cluster<MapMarker>, marker: Marker) {
        clusterPhotoIcon.setCounter(cluster.size)
        iconCache.get(cluster.items.first().photo)?.let(clusterPhotoIcon::setImage)
        val icon = clusterPhotoIconGenerator.makeIcon()
        marker.setIcon(BitmapDescriptorFactory.fromBitmap(icon))
    }

    override fun onBeforeClusterRendered(
        cluster: Cluster<MapMarker>,
        markerOptions: MarkerOptions
    ) {
        iconCache.put(cluster.items.first().photo, true)
        clusterPhotoIcon.setCounter(cluster.size)
        clusterPhotoIcon.setBackground(R.drawable.bg_cluster_gray)
        val icon = clusterPhotoIconGenerator.makeIcon()
        markerOptions.icon(BitmapDescriptorFactory.fromBitmap(icon))
    }
}
