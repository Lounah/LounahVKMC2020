package com.lounah.vkmc.feature.feature_image_picker.presentation

import com.freeletics.rxredux.SideEffect
import com.freeletics.rxredux.reduxStore
import com.jakewharton.rxrelay2.PublishRelay
import com.lounah.vkmc.feature.feature_image_picker.presentation.ImagePickerAction.*
import com.lounah.vkmc.feature.feature_image_picker.presentation.ImagePickerEvent.*
import com.lounah.vkmc.feature.feature_image_picker.ui.util.Value
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.rxkotlin.ofType
import java.io.File

private typealias ImagePickerSideEff = SideEffect<ImagePickerState, ImagePickerAction>

class ImagePickerPresenter(
    private val getGalleryPhotos: () -> Single<List<String>>,
    private val takenFromCameraPhoto: Value<File>
) {

    val input = PublishRelay.create<ImagePickerAction>()
    val events = PublishRelay.create<ImagePickerEvent>()
    val state: Observable<ImagePickerState> =
        input.reduxStore(
            initialState = ImagePickerState(),
            sideEffects = listOf(loadGalleryPhotos(), requestCameraAccess(), showPickedImage()),
            reducer = ImagePickerState::reduce
        ).distinctUntilChanged()

    private fun loadGalleryPhotos(): ImagePickerSideEff {
        return { actions, _ ->
            actions.ofType<RequestGalleryPhotos>().flatMap<ImagePickerAction> {
                getGalleryPhotos()
                    .map(::OnImagesLoaded)
                    .doOnError { events.accept(ShowError(it)) }
                    .toObservable()
                    .onErrorResumeNext(Observable.empty())
            }
        }
    }

    private fun requestCameraAccess(): ImagePickerSideEff {
        return { actions, _ ->
            actions.ofType<OnPhotoPickerClicked>().flatMap {
                events.accept(RequestCameraAccess)
                Observable.empty<ImagePickerAction>()
            }
        }
    }

    private fun showPickedImage(): ImagePickerSideEff {
        return { actions, _ ->
            actions.ofType<OnImageSelected>().flatMap {
                events.accept(ShowSelectedImage(it.path))
                Observable.empty<ImagePickerAction>()
            }
        }
    }
}