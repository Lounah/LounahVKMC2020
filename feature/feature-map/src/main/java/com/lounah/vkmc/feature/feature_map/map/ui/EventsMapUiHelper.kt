package com.lounah.vkmc.feature.feature_map.map.ui

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.lounah.vkmc.core.extensions.asType
import com.lounah.vkmc.core.extensions.toggleTranslationY
import com.lounah.vkmc.feature.feature_map.R
import com.lounah.vkmc.feature.feature_map.map.ui.map.EventsMapClusterManager
import com.lounah.vkmc.feature.feature_map.map.ui.map.MapMarker
import com.lounah.vkmc.feature.feature_map.map.ui.map.MapMarkerRenderer
import kotlinx.android.synthetic.main.activity_events_map.*


private val DEFAULT_MAP_POINT = LatLng(45.009750, 39.037506)
private const val DEFAULT_ZOOM_LEVEL = 16f

internal class EventsMapUiHelper(
    private val activity: EventsMapActivity,
    private val onClusterClicked: (Collection<MapMarker>) -> Unit,
    private val onClusterItemClicked: (MapMarker) -> Unit
) {

    val currentLocation: LatLng
        get() = gMap.cameraPosition.target

    private lateinit var gMap: GoogleMap
    private lateinit var clusterer: EventsMapClusterManager

    private val tabsContainer = activity.tabsContainer

    init {
        activity.supportFragmentManager.findFragmentById(R.id.map)
            .asType<SupportMapFragment>()
            .also { it.retainInstance = true }
            .getMapAsync(activity)
    }

    fun renderMarkers(markers: List<MapMarker>) {
        if (::clusterer.isInitialized) {
            clusterer.addItems(markers)
            clusterer.cluster()
        }
    }

    fun handleMapLoaded(map: GoogleMap?, onMapIdle: () -> Unit) {
        map?.run {
            gMap = this
            moveCamera(CameraUpdateFactory.newLatLngZoom(DEFAULT_MAP_POINT, DEFAULT_ZOOM_LEVEL))
            gMap.setOnMapClickListener {
                tabsContainer.toggleTranslationY(250)
            }
            setUpClusterer(onMapIdle)
        }
    }

    fun refreshMap() {
        gMap.clear()
        clusterer.clearItems()
    }

    private fun setUpClusterer(onMapIdle: () -> Unit) {
        clusterer = EventsMapClusterManager(activity, gMap, onMapIdle)
        clusterer.setAnimation(true)
        gMap.setOnCameraIdleListener(clusterer)
        gMap.setOnMarkerClickListener(clusterer)
        clusterer.renderer = MapMarkerRenderer(clusterer, activity, gMap)
        clusterer.setOnClusterItemClickListener {
            gMap.animateCamera(CameraUpdateFactory.newLatLngZoom(it.latlng, DEFAULT_ZOOM_LEVEL),
                1000, object : GoogleMap.CancelableCallback {
                override fun onFinish() = onClusterItemClicked(it)
                override fun onCancel() = Unit
            })
            true
        }

        clusterer.setOnClusterClickListener {
            gMap.animateCamera(CameraUpdateFactory.newLatLngZoom(it.items.first().latlng, DEFAULT_ZOOM_LEVEL),
                1000, object : GoogleMap.CancelableCallback {
                override fun onFinish() = onClusterClicked(it.items)
                override fun onCancel() = Unit
            })
            true
        }
    }
}
