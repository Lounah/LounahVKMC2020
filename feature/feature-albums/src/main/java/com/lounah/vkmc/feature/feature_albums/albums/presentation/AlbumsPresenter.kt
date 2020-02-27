package com.lounah.vkmc.feature.feature_albums.albums.presentation

import com.freeletics.rxredux.SideEffect
import com.freeletics.rxredux.reduxStore
import com.jakewharton.rxrelay2.PublishRelay
import com.lounah.vkmc.core.core_vk.domain.Offset
import com.lounah.vkmc.core.core_vk.model.Album
import com.lounah.vkmc.feature.feature_albums.albums.presentation.AlbumsAction.*
import com.lounah.vkmc.feature.feature_albums.albums.ui.recycler.holder.AlbumUi
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.Consumer
import io.reactivex.rxkotlin.ofType
import io.reactivex.schedulers.Schedulers

private typealias AlbumsSideEffect = SideEffect<AlbumsState, AlbumsAction>

class AlbumsPresenter(
    private val getAlbums: (Offset) -> Single<List<Album>>,
    private val albumsMapper: (List<Album>) -> List<AlbumUi>
) {

    private val inputRelay = PublishRelay.create<AlbumsAction>()

    val input: Consumer<AlbumsAction> = inputRelay
    val state: Observable<AlbumsState> = inputRelay
        .reduxStore(
            initialState = AlbumsState(),
            sideEffects = listOf(
                loadPagedAlbums(),
                initialLoading(),
                repeatLoadAlbums()
            ),
            reducer = AlbumsState::reduce
        ).distinctUntilChanged()

    private fun initialLoading(): AlbumsSideEffect {
        return { _, _ -> loadAlbums(0) }
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
            .subscribeOn(Schedulers.single())
            .observeOn(AndroidSchedulers.mainThread())
            .toObservable()
            .map<AlbumsAction> { OnAlbumsLoaded(albumsMapper(it)) }
            .onErrorReturnItem(OnLoadingError)
            .startWith(OnLoadingStarted)
    }
}
