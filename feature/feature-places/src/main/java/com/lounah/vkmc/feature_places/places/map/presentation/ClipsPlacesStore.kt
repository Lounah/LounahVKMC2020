package com.lounah.vkmc.feature_places.places.map.presentation

import com.lounah.vkmc.core.arch.RxStore
import com.lounah.vkmc.core.arch.impl.rxStore
import com.lounah.vkmc.feature_places.places.map.presentation.PlacesCommand.PlacesNews
import com.lounah.vkmc.feature_places.places.map.presentation.cmdhandler.LoadStoriesByLocationCommandHandler

class ClipsPlacesStore(
    private val loadStoriesCommandHandler: LoadStoriesByLocationCommandHandler
) : RxStore<PlacesState, PlacesEvent, PlacesNews> by rxStore(
    initialState = PlacesState(),
    commandsHandlers = listOf(loadStoriesCommandHandler),
    update = PlacesUpdater()
)
