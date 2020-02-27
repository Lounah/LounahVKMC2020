package com.lounah.vkmc.feature.feature_albums.photos.presentation

import com.freeletics.rxredux.SideEffect
import com.freeletics.rxredux.reduxStore
import com.jakewharton.rxrelay2.PublishRelay
import com.lounah.vkmc.core.core_vk.domain.AlbumId
import com.lounah.vkmc.core.core_vk.domain.Offset
import com.lounah.vkmc.core.core_vk.model.Photo
import com.lounah.vkmc.feature.feature_albums.photos.presentation.PhotosAction.*
import com.lounah.vkmc.feature.feature_albums.photos.ui.recycler.PhotoUi
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.Consumer
import io.reactivex.rxkotlin.ofType
import io.reactivex.schedulers.Schedulers

private typealias PhotosSideEffect = SideEffect<PhotosState, PhotosAction>

class PhotosPresenterFactory(
    private val getPhotos: (Offset, AlbumId) -> Single<List<Photo>>,
    private val photosMapper: (List<Photo>) -> List<PhotoUi>
) : (String, AlbumId) -> PhotosPresenter {

    override fun invoke(albumName: String, albumId: AlbumId): PhotosPresenter {
        return PhotosPresenter(albumName, albumId, getPhotos, photosMapper)
    }
}

class PhotosPresenter(
    albumName: String,
    private val albumId: AlbumId,
    private val getPhotos: (Offset, AlbumId) -> Single<List<Photo>>,
    private val photosMapper: (List<Photo>) -> List<PhotoUi>
) {

    private val inputRelay = PublishRelay.create<PhotosAction>()

    val input: Consumer<PhotosAction> = inputRelay
    val state: Observable<PhotosState> = inputRelay
        .reduxStore(
            initialState = PhotosState(albumName, albumId),
            sideEffects = listOf(
                loadPagedPhotos(),
                initialLoading(),
                repeatLoadPhotos()
            ),
            reducer = PhotosState::reduce
        ).distinctUntilChanged()

    private fun initialLoading(): PhotosSideEffect {
        return { _, _ -> loadPhotos(0, albumId) }
    }

    private fun loadPagedPhotos(): PhotosSideEffect {
        return { actions, _ ->
            actions.ofType<OnNextPage>().flatMap { loadPhotos(it.offset, albumId) }
        }
    }

    private fun repeatLoadPhotos(): PhotosSideEffect {
        return { actions, state ->
            actions.ofType<OnRepeatLoadClicked>()
                .flatMap { loadPhotos(state().offset, albumId) }
        }
    }

    private fun loadPhotos(offset: Int, albumId: AlbumId): Observable<PhotosAction> {
        return getPhotos(offset, albumId)
            .subscribeOn(Schedulers.single())
            .observeOn(AndroidSchedulers.mainThread())
            .toObservable()
            .map<PhotosAction> { OnPhotosLoaded(photosMapper(it)) }
            .onErrorReturnItem(OnLoadingError)
            .startWith(OnLoadingStarted)
    }
}
