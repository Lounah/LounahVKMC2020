package com.lounah.vkmc.feature.feature_albums.albums.presentation

import com.freeletics.rxredux.SideEffect
import com.freeletics.rxredux.reduxStore
import com.jakewharton.rxrelay2.PublishRelay
import com.lounah.vkmc.core.core_vk.domain.AlbumId
import com.lounah.vkmc.core.core_vk.domain.groups.Offset
import com.lounah.vkmc.core.core_vk.model.Album
import com.lounah.vkmc.feature.feature_albums.albums.presentation.AlbumsAction.*
import com.lounah.vkmc.feature.feature_albums.albums.presentation.AlbumsEvent.ErrorDeletingAlbum
import com.lounah.vkmc.feature.feature_albums.albums.ui.recycler.holder.AlbumUi
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.Consumer
import io.reactivex.rxkotlin.ofType
import io.reactivex.schedulers.Schedulers.single

private typealias AlbumsSideEffect = SideEffect<AlbumsState, AlbumsAction>

class AlbumsPresenter(
    private val deleteAlbum: (AlbumId) -> Single<Boolean>,
    private val getAlbums: (Offset) -> Single<List<Album>>,
    private val albumsMapper: (List<Album>) -> List<AlbumUi>
) {

    private val inputRelay = PublishRelay.create<AlbumsAction>()
    private val eventsRelay = PublishRelay.create<AlbumsEvent>()

    val input: Consumer<AlbumsAction> = inputRelay
    val events: Observable<AlbumsEvent> = eventsRelay
    val state: Observable<AlbumsState> = inputRelay
        .reduxStore(
            initialState = AlbumsState(),
            sideEffects = listOf(
                loadPagedAlbums(),
                initialLoading(),
                repeatLoadAlbums(),
                onDeleteAlbumClicked()
            ),
            reducer = AlbumsState::reduce
        ).distinctUntilChanged()

    private fun onDeleteAlbumClicked(): AlbumsSideEffect {
        return { actions, _ ->
            actions.ofType<OnDeleteAlbumClicked>().switchMap { onDelete ->
                deleteAlbum(onDelete.albumId)
                    .subscribeOn(single())
                    .doOnError { eventsRelay.accept(ErrorDeletingAlbum) }
                    .onErrorReturnItem(false)
                    .flatMapObservable { deleted ->
                        if (deleted) {
                            Observable.just(OnAlbumDeleted(onDelete.albumId))
                        } else {
                            Observable.empty()
                        }
                    }
            }
        }
    }

    private fun initialLoading(): AlbumsSideEffect {
        return { actions, _ ->
            actions.ofType<LoadAlbums>().switchMap {
                loadAlbums(0)
            }
        }
    }

    private fun loadPagedAlbums(): AlbumsSideEffect {
        return { actions, _ ->
            actions.ofType<OnNextPage>().flatMap { loadAlbums(it.offset) }
        }
    }

    private fun repeatLoadAlbums(): AlbumsSideEffect {
        return { actions, state ->
            actions.ofType<OnRepeatLoadClicked>()
                .flatMap { loadAlbums(state().offset) }
        }
    }

    private fun loadAlbums(offset: Int): Observable<AlbumsAction> {
        return getAlbums(offset)
            .subscribeOn(single())
            .observeOn(AndroidSchedulers.mainThread())
            .toObservable()
            .map<AlbumsAction> { OnAlbumsLoaded(albumsMapper(it)) }
            .onErrorReturnItem(OnLoadingError)
            .startWith(OnLoadingStarted)
    }
}
