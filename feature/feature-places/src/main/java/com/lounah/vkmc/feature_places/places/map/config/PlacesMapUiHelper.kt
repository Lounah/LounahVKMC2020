package com.lounah.vkmc.feature_places.places.map.config

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.lounah.vkmc.core.extensions.asType
import com.lounah.vkmc.feature.feature_places.R
import com.lounah.vkmc.feature_places.places.map.config.markers.StoriesClusterItem
import com.lounah.vkmc.feature_places.places.map.ui.ClipsPlacesFragment

private val DEFAULT_MAP_POINT = LatLng(45.009750, 39.037506) // Krasnodar
private const val DEFAULT_ZOOM_LEVEL = 16f

internal class PlacesMapUiHelper(
    private val fragment: ClipsPlacesFragment,
    private val storiesRenderer: ClusterRenderer<StoriesClusterItem>,
    private val onClusterClicked: (Collection<StoriesClusterItem>) -> Unit,
    private val onClusterItemClicked: (StoriesClusterItem) -> Unit
) {

    val currentLocation get() = map.cameraPosition.target

    private lateinit var map: GoogleMap
    private lateinit var clusterer: StoriesMapClusterManager
    private val markersInternal = mutableSetOf<StoriesClusterItem>()

    init {
        fragment.childFragmentManager.findFragmentById(R.id.map)
            .asType<SupportMapFragment>()
            .run {
                retainInstance = true
                getMapAsync(fragment)
            }
    }

    fun renderMarkers(markers: List<StoriesClusterItem>) {
        if (::clusterer.isInitialized) {
            val existed = markers.filter(markersInternal::contains)
            if (existed.isEmpty()) {
                clusterer.addItems(markers)
                clusterer.cluster()
                markersInternal.addAll(markers)
            }
        }
    }

    fun handleMapLoaded(map: GoogleMap?, onMapIdle: () -> Unit) {
        map?.run {
            this@PlacesMapUiHelper.map = this
            moveCamera(CameraUpdateFactory.newLatLngZoom(DEFAULT_MAP_POINT, DEFAULT_ZOOM_LEVEL))
            setUpClusterer(onMapIdle)
        }
    }

    fun refreshMap() {
        map.clear()
        clusterer.clearItems()
        markersInternal.clear()
        clusterer.renderer.onClustersChanged(emptySet())
    }

    private fun setUpClusterer(onMapIdle: () -> Unit) {
        clusterer = StoriesMapClusterManager(fragment.requireContext(), map, onMapIdle)
        clusterer.setAnimation(true)
        val renderer = MapMarkerRenderer(clusterer, fragment.requireContext(), map, storiesRenderer)
        map.setOnCameraIdleListener(clusterer)
        map.setOnMarkerClickListener(clusterer)
        clusterer.renderer = renderer
        clusterer.setOnClusterItemClickListener {
            map.animateCamera(
                CameraUpdateFactory.newLatLngZoom(it.position, DEFAULT_ZOOM_LEVEL),
                1000, object : GoogleMap.CancelableCallback {
                    override fun onFinish() = onClusterItemClicked(it)
                    override fun onCancel() = Unit
                })
            true
        }

        clusterer.setOnClusterClickListener {
            map.animateCamera(
                CameraUpdateFactory.newLatLngZoom(
                it.items.first().position,
                DEFAULT_ZOOM_LEVEL
            ),
                1000, object : GoogleMap.CancelableCallback {
                    override fun onFinish() = onClusterClicked(it.items)
                    override fun onCancel() = Unit
                })
            true
        }
    }
}
