package com.lounah.vkmc.feature.feature_map.map.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.WindowManager.LayoutParams.FLAG_FULLSCREEN
import android.view.WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.material.snackbar.Snackbar
import com.lounah.vkmc.core.di.ComponentStorage.getComponent
import com.lounah.vkmc.core.extensions.disposeOnDestroy
import com.lounah.vkmc.core.extensions.dp
import com.lounah.vkmc.core.extensions.setVisible
import com.lounah.vkmc.core.extensions.subscribeTo
import com.lounah.vkmc.feature.feature_map.R
import com.lounah.vkmc.feature.feature_map.details.ui.GroupOrEventDetailsDialog
import com.lounah.vkmc.feature.feature_map.di.EventsMapComponent
import com.lounah.vkmc.feature.feature_map.map.presentation.EventsMapAction.*
import com.lounah.vkmc.feature.feature_map.map.presentation.EventsMapPresenter
import com.lounah.vkmc.feature.feature_map.map.presentation.EventsMapState
import com.lounah.vkmc.feature.feature_map.map.presentation.RenderType
import com.lounah.vkmc.feature.feature_map.map.ui.map.markers.MapMarker
import com.lounah.vkmc.feature.image_viewer.ui.ImageViewerActivity
import com.vk.api.sdk.auth.VKScope.*
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.activity_events_map.*
import kotlin.LazyThreadSafetyMode.NONE


class EventsMapActivity : AppCompatActivity(), OnMapReadyCallback {

    private val presenter: EventsMapPresenter by lazy(NONE) {
        getComponent<EventsMapComponent>().eventsMapPresenter
    }

    private val isFirstLaunch = getComponent<EventsMapComponent>().isFirstLaunch

    private val uiHelper: EventsMapUiHelper by lazy(NONE) {
        EventsMapUiHelper(this, ::onClusterClicked, ::onClusterItemClicked)
    }

    private val errorSnackbar: Snackbar by lazy(NONE) {
        Snackbar.make(root, R.string.error_loading, Snackbar.LENGTH_INDEFINITE)
            .setAction(R.string.retry) {
                presenter.input.accept(OnRetryLoading(uiHelper.currentLocation))
            }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        overridePendingTransition(0, 0)
        window.setFlags(FLAG_LAYOUT_NO_LIMITS, FLAG_LAYOUT_NO_LIMITS)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_events_map)
        initUi()
        initBindings()
    }

    private fun initBindings() {
        presenter.state
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeTo(onNext = ::render)
            .disposeOnDestroy(this)
    }

    private fun render(state: EventsMapState) {
        uiHelper.renderMarkers(state.items)
        progressView.setVisible(state.isLoading)
        if (state.isLoading) {
            window.setFlags(FLAG_FULLSCREEN, FLAG_FULLSCREEN)
        } else {
            window.clearFlags(FLAG_FULLSCREEN)
        }
        if (state.hasError && !errorSnackbar.isShown) {
            errorSnackbar.show()
        }
    }

    override fun onMapReady(map: GoogleMap?) {
        uiHelper.handleMapLoaded(map, onMapIdle = {
            presenter.input.accept(OnMapIdle(uiHelper.currentLocation))
        })
    }

    private fun initUi() {
        tabs.tabsMargins(16.dp(this), 0, 16.dp(this), 0)
        tabs.onTabSelected {
            uiHelper.refreshMap()
            when (it) {
                0 -> OnRenderTypeChanged(RenderType.EVENTS)
                1 -> OnRenderTypeChanged(RenderType.PHOTOS)
                2 -> OnRenderTypeChanged(RenderType.GROUPS)
                else -> error("Unknown tab position")
            }.also(presenter.input::accept)
        }
        if (isFirstLaunch) FeatureRationaleDialog().show(supportFragmentManager, null)
    }

    private fun onClusterClicked(items: Collection<MapMarker>) {
        if (items.any { it.isPhoto }) {
            ImageViewerActivity.start(this, ArrayList(items.map { it.photoLarge }))
        }
    }

    private fun onClusterItemClicked(item: MapMarker) {
        if (item.isPhoto) {
            ImageViewerActivity.start(this, arrayListOf(item.photoLarge))
        } else {
            GroupOrEventDetailsDialog.newInstance(item.id, item.address)
                .show(supportFragmentManager, null)
        }
    }

    companion object {
        val authScopes = listOf(GROUPS, PHOTOS, WALL, STORIES)

        fun start(context: Context) {
            Intent(context, EventsMapActivity::class.java).also(context::startActivity)
        }
    }
}
