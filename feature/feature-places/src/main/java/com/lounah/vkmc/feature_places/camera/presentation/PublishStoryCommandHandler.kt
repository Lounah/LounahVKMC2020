package com.lounah.vkmc.feature_places.camera.presentation

import com.google.android.gms.maps.model.LatLng
import com.lounah.vkmc.core.arch.CommandsHandler
import com.lounah.vkmc.feature_places.camera.domain.StoryCreator
import com.lounah.vkmc.feature_places.camera.presentation.StorySettingsCommand.PublishStory
import com.lounah.vkmc.feature_places.camera.presentation.StorySettingsEvent.OnStoryLoadingFailed
import com.lounah.vkmc.feature_places.camera.presentation.StorySettingsEvent.OnStoryUploaded
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers.mainThread
import io.reactivex.rxkotlin.ofType
import io.reactivex.schedulers.Schedulers.io
import java.io.File

class PublishStoryCommandHandler(
    private val createThumbnail: (File) -> Observable<String>,
    private val storyCreator: StoryCreator
) : CommandsHandler<StorySettingsCommand, StorySettingsEvent> {

    override fun handle(commands: Observable<StorySettingsCommand>): Observable<out StorySettingsEvent> {
        return commands.ofType<PublishStory>().switchMap { command ->
            createThumbnail(command.story).flatMap { thumbnailUri ->
                storyCreator.create(
                    command.title,
                    command.comment,
                    thumbnailUri,
                    LatLng(command.lat, command.lng),
                    command.story
                )
                    .toObservable<StorySettingsEvent>()
                    .map<StorySettingsEvent> { OnStoryUploaded }
                    .onErrorReturnItem(OnStoryLoadingFailed)
                    .subscribeOn(io())
                    .observeOn(mainThread())
            }
        }
    }
}
