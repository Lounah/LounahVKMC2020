package com.lounah.vkmc.feature_places.places.map.ui

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.material.snackbar.Snackbar
import com.lounah.vkmc.core.arch.lifecycle.bind
import com.lounah.vkmc.core.di.ComponentStorage.getComponent
import com.lounah.vkmc.feature.feature_places.R
import com.lounah.vkmc.feature_places.di.ClipsPlacesComponent
import com.lounah.vkmc.feature_places.places.map.config.PlacesMapUiHelper
import com.lounah.vkmc.feature_places.places.map.config.StoriesClusterRenderer
import com.lounah.vkmc.feature_places.places.map.config.markers.StoriesClusterItem
import com.lounah.vkmc.feature_places.places.map.presentation.ClipsPlacesStore
import com.lounah.vkmc.feature_places.places.map.presentation.PlacesCommand.PlacesNews
import com.lounah.vkmc.feature_places.places.map.presentation.PlacesCommand.PlacesNews.ShowError
import com.lounah.vkmc.feature_places.places.map.presentation.PlacesEvent.LoadStories
import com.lounah.vkmc.feature_places.places.map.presentation.PlacesState
import com.lounah.vkmc.feature_places.places.preview.ui.StoriesDialog
import com.robertlevonyan.views.chip.OnSelectClickListener
import kotlinx.android.synthetic.main.fragment_clips_places.*
import kotlin.LazyThreadSafetyMode.NONE

internal class ClipsPlacesFragment : Fragment(R.layout.fragment_clips_places), OnMapReadyCallback{

    private val store: ClipsPlacesStore by lazy(NONE) {
        getComponent<ClipsPlacesComponent>().store
    }

    private val uiHelper: PlacesMapUiHelper by lazy(NONE) {
        PlacesMapUiHelper(
            this,
            StoriesClusterRenderer(requireContext()),
            ::onClusterClicked,
            ::onClusterItemClicked
        )
    }

    private val errorSnackbar: Snackbar by lazy(NONE) {
        Snackbar.make(root, R.string.error_loading, Snackbar.LENGTH_INDEFINITE)
            .setAction(R.string.retry) {
                store.dispatch(LoadStories(uiHelper.currentLocation))
            }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUI()
    }

    override fun onMapReady(map: GoogleMap) {
        uiHelper.handleMapLoaded(map, onMapIdle = {
            if (!photos.chipSelected && !friends.chipSelected) {
                store.dispatch(LoadStories(uiHelper.currentLocation))
            }
        })
    }

    private fun initUI() {
        store.bind(viewLifecycleOwner, ::render, ::handleNews)
        initFilters()
    }

    private fun render(state: PlacesState) {
        progressView.isVisible = state.loading
        uiHelper.renderMarkers(state.stories)
    }

    private fun handleNews(news: PlacesNews) {
        when (news) {
            is ShowError -> errorSnackbar.show()
        }
    }

    private fun onClusterClicked(storiesCluster: Collection<StoriesClusterItem>) {
        StoriesDialog.newInstance(storiesCluster.map(StoriesClusterItem::id)).show(childFragmentManager, null)
    }

    private fun onClusterItemClicked(story: StoriesClusterItem) {
        StoriesDialog.newInstance(listOf(story.id)).show(childFragmentManager, null)
    }

    // Грязные хаки, опять же некрасивые, и на ревью за такое отрывают руки))0
    // но это хакатон, так что все равно
    // фото и фильтры от друзей не поддерживаем, а hot – пока что все истории/клипы
    private fun initFilters() {
        photos.onSelectClickListener = OnSelectClickListener { _, selected ->
            if (selected || friends.chipSelected) {
                uiHelper.refreshMap()
            } else if (!friends.chipSelected) {
                store.dispatch(LoadStories(uiHelper.currentLocation, force = true))
            }
        }
        hot.onSelectClickListener = OnSelectClickListener { _, selected ->
            if (photos.chipSelected || friends.chipSelected) {
                uiHelper.refreshMap()
            } else {
                store.dispatch(LoadStories(uiHelper.currentLocation, force = true))
            }
        }
        friends.onSelectClickListener = OnSelectClickListener { _, selected ->
            if (selected || photos.chipSelected) {
                uiHelper.refreshMap()
            } else if (!photos.chipSelected) {
                store.dispatch(LoadStories(uiHelper.currentLocation, force = true))
            }
        }
    }
}

