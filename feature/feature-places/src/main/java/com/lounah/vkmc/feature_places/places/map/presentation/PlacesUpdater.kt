package com.lounah.vkmc.feature_places.places.map.presentation

import com.lounah.vkmc.core.arch.Next
import com.lounah.vkmc.core.arch.Update
import com.lounah.vkmc.feature_places.places.map.presentation.PlacesCommand.LoadStoriesByLocation
import com.lounah.vkmc.feature_places.places.map.presentation.PlacesCommand.PlacesNews.ShowError
import com.lounah.vkmc.feature_places.places.map.presentation.PlacesEvent.*

internal class PlacesUpdater : Update<PlacesState, PlacesEvent, PlacesCommand> {

    override fun update(state: PlacesState, event: PlacesEvent): Next<PlacesState, PlacesCommand> {
        return when (event) {
            is LoadingFailed -> {
                println("ERROR: ${event.error}")
                Next(state.copy(loading = false), listOf(ShowError))
            }
            is HideLoading -> Next(state.copy(loading = false))
            is StoriesLoaded -> {
                val cityId = event.places.firstOrNull()?.cityId.orEmpty()
                Next(state.copy(loading = false, stories = event.places.distinct(), currentCityId = cityId))
            }
            is LoadStories -> {
                Next(state.copy(loading = true), listOf(LoadStoriesByLocation(event.location, state.currentCityId, event.force)))
            }
        }
    }
}
