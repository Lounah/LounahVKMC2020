package com.lounah.vkmc.feature_places.places.map.presentation.cmdhandler

import com.lounah.vkmc.core.arch.CommandsHandler
import com.lounah.vkmc.feature_places.places.map.config.markers.StoriesClusterItem
import com.lounah.vkmc.feature_places.places.map.domain.GetNearestCity
import com.lounah.vkmc.feature_places.places.map.domain.StoriesRepository
import com.lounah.vkmc.feature_places.places.map.presentation.PlacesCommand
import com.lounah.vkmc.feature_places.places.map.presentation.PlacesCommand.LoadStoriesByLocation
import com.lounah.vkmc.feature_places.places.map.presentation.PlacesEvent
import com.lounah.vkmc.feature_places.places.map.presentation.PlacesEvent.*
import com.lounah.vkmc.feature_places.places.model.Story
import io.reactivex.Observable
import io.reactivex.rxkotlin.ofType

class LoadStoriesByLocationCommandHandler(
    private val storiesRepository: StoriesRepository,
    private val getNearestCity: GetNearestCity,
    private val storiesMapper: (List<Story>) -> List<StoriesClusterItem>
) : CommandsHandler<PlacesCommand, PlacesEvent> {

    override fun handle(commands: Observable<PlacesCommand>): Observable<out PlacesEvent> {
        return commands.ofType<LoadStoriesByLocation>().switchMap { command ->
            val city = getNearestCity(command.location)
            if (command.force || city != command.cityId) {
                storiesRepository.getNearestStories(command.location)
                    .map<PlacesEvent> { StoriesLoaded(storiesMapper(it)) }
                    .onErrorReturn(::LoadingFailed)
            } else Observable.just(HideLoading)
        }
    }
}
