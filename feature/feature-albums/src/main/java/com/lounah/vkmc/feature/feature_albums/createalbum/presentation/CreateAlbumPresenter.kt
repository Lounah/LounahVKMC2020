package com.lounah.vkmc.feature.feature_albums.createalbum.presentation

import com.freeletics.rxredux.SideEffect
import com.freeletics.rxredux.reduxStore
import com.jakewharton.rxrelay2.PublishRelay
import com.lounah.vkmc.core.core_vk.domain.CreateAlbumBody
import com.lounah.vkmc.feature.feature_albums.createalbum.presentation.CreateAlbumAction.OnCreateClicked
import com.lounah.vkmc.feature.feature_albums.createalbum.presentation.CreateAlbumAction.OnCreateFailed
import com.lounah.vkmc.feature.feature_albums.createalbum.presentation.CreateAlbumEvent.OnCreateSucceed
import com.lounah.vkmc.feature.feature_albums.createalbum.presentation.CreateAlbumEvent.ShowError
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.functions.Consumer
import io.reactivex.rxkotlin.ofType

typealias CreateAlbumSideEffect = SideEffect<CreateAlbumState, CreateAlbumAction>

class CreateAlbumPresenter(
    private val createAlbum: (CreateAlbumBody) -> Single<String>
) {

    private val inputRelay = PublishRelay.create<CreateAlbumAction>()
    private val eventsRelay = PublishRelay.create<CreateAlbumEvent>()

    val input: Consumer<CreateAlbumAction> = inputRelay
    val events: Observable<CreateAlbumEvent> = eventsRelay
    val state: Observable<CreateAlbumState> = inputRelay
        .reduxStore(
            initialState = CreateAlbumState(),
            sideEffects = listOf(onCreateClicked()),
            reducer = CreateAlbumState::reduce
        ).distinctUntilChanged()

    private fun onCreateClicked(): CreateAlbumSideEffect {
        return { actions, state ->
            actions.ofType<OnCreateClicked>().switchMap {
                val createBody = state().run { CreateAlbumBody(title, description, isPrivate) }
                createAlbum(createBody)
                    .doOnError { eventsRelay.accept(ShowError) }
                    .doOnSuccess { eventsRelay.accept(OnCreateSucceed) }
                    .flatMapObservable { Observable.empty<CreateAlbumAction>() }
                    .onErrorReturnItem(OnCreateFailed)
            }
        }
    }
}
