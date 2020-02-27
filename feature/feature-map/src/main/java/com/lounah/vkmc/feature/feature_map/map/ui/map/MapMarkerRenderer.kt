package com.lounah.vkmc.feature.feature_map.map.ui.map

import android.content.Context
import android.graphics.Bitmap
import android.view.ViewGroup
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.maps.android.clustering.Cluster
import com.google.maps.android.clustering.ClusterManager
import com.google.maps.android.clustering.view.DefaultClusterRenderer
import com.google.maps.android.ui.IconGenerator
import com.lounah.vkmc.core.extensions.asType
import com.lounah.vkmc.core.extensions.dp
import com.lounah.vkmc.core.ui.imageloader.GlideApp
import com.lounah.vkmc.feature.feature_map.R
import java.util.concurrent.ConcurrentHashMap

private val bitmapCache = ConcurrentHashMap<String, Bitmap>()

private val placeholders =
    listOf(R.drawable.bg_cluster_blue, R.drawable.bg_cluster_gray, R.drawable.bg_cluster_purple)

class MapMarkerRenderer(
    private val clusterManager: ClusterManager<MapMarker>,
    private val context: Context,
    map: GoogleMap
) : DefaultClusterRenderer<MapMarker>(context, map, clusterManager) {

    private val groupIconSizeDp = 32.dp(context)
    private val photoIconSizeDp = 58.dp(context)

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
        super.onClusterItemRendered(clusterItem, marker)
        val (miniIcon, generator) = if (!clusterItem.isPhoto) {
            singleGroupIcon to groupIconGenerator
        } else {
            singlePhotoIcon to photoIconGenerator
        }.asType<Pair<MarkerView, IconGenerator>>()
        bitmapCache[clusterItem.photo]?.let(miniIcon::setImage)
            ?: miniIcon.setIcon(clusterItem.photo)
        val icon = generator.makeIcon()
        marker.setIcon(BitmapDescriptorFactory.fromBitmap(icon))
    }

    override fun onBeforeClusterItemRendered(item: MapMarker, markerOptions: MarkerOptions) {
        putIconIntoCacheIfNecessary(item)
        val (miniIcon, generator) = if (!item.isPhoto) {
            singleGroupIcon to groupIconGenerator
        } else {
            singlePhotoIcon to photoIconGenerator
        }.asType<Pair<MarkerView, IconGenerator>>()
        if (!item.isPhoto) {
            miniIcon.setBackground(R.drawable.bg_cluster_gray)
        }
        bitmapCache[item.photo]?.let(miniIcon::setImage)
            ?: miniIcon.setIcon(item.photo)
        val icon = generator.makeIcon()
        markerOptions.icon(BitmapDescriptorFactory.fromBitmap(icon))
    }

    override fun onClusterRendered(cluster: Cluster<MapMarker>, marker: Marker) {
        super.onClusterRendered(cluster, marker)
        val header = cluster.items.first()
        val (miniIcon, generator) = if (header.isPhoto) {
            clusterPhotoIcon to clusterPhotoIconGenerator
        } else {
            clusterGroupIcon to clusterGroupIconGenerator
        }.asType<Pair<MarkerView, IconGenerator>>()
        miniIcon.setCounter(cluster.size)
        if (header.isPhoto) {
            bitmapCache[header.photo]?.let(miniIcon::setImage) ?: miniIcon.setIcon(header.photo)
        }
        val icon = generator.makeIcon()
        marker.setIcon(BitmapDescriptorFactory.fromBitmap(icon))
    }

    override fun onBeforeClusterRendered(
        cluster: Cluster<MapMarker>,
        markerOptions: MarkerOptions
    ) {
        val header = cluster.items.first()
        putIconIntoCacheIfNecessary(header)
        val (miniIcon, generator) = if (header.isPhoto) {
            clusterPhotoIcon to clusterPhotoIconGenerator
        } else {
            clusterGroupIcon to clusterGroupIconGenerator
        }.asType<Pair<MarkerView, IconGenerator>>()
        miniIcon.setCounter(cluster.size)
        if (header.isPhoto) {
            bitmapCache[header.photo]?.let(miniIcon::setImage) ?: miniIcon.setIcon(header.photo)
        } else {
            miniIcon.setBackground(placeholders.random())
        }
        val icon = generator.makeIcon()
        markerOptions.icon(BitmapDescriptorFactory.fromBitmap(icon))
    }

    override fun shouldRenderAsCluster(cluster: Cluster<MapMarker>): Boolean = cluster.size > 1

    private fun putIconIntoCacheIfNecessary(item: MapMarker) {
        if (bitmapCache[item.photo] == null)
            GlideApp.with(context).asBitmap().load(item.photo)
                .also { if (!item.isPhoto) it.circleCrop() }
                .into(object : SimpleTarget<Bitmap>() {
                    override fun onResourceReady(
                        resource: Bitmap,
                        transition: Transition<in Bitmap>?
                    ) {
                        bitmapCache[item.photo] = resource
                    }
                })
    }
}
