package com.lounah.vkmc.feature.feature_sharing.presentation

import android.net.Uri
import android.util.Log
import com.freeletics.rxredux.SideEffect
import com.freeletics.rxredux.reduxStore
import com.jakewharton.rxrelay2.PublishRelay
import com.lounah.vkmc.core.core_vk.domain.WallPost
import com.lounah.vkmc.feature.feature_sharing.presentation.ShareMediaAction.OnShareWallPostClicked
import com.lounah.vkmc.feature.feature_sharing.presentation.ShareMediaAction.UploadError
import com.lounah.vkmc.feature.feature_sharing.presentation.ShareMediaEvent.OnPostSuccessfullyShared
import com.lounah.vkmc.feature.feature_sharing.presentation.ShareMediaEvent.ShowUploadingError
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.functions.Consumer
import io.reactivex.rxkotlin.ofType
import io.reactivex.schedulers.Schedulers.single

private typealias SharingSideEffect = SideEffect<ShareMediaState, ShareMediaAction>

class ShareMediaPresenter(
    private val createWallPost: (String, List<Uri>) -> Single<WallPost>
) {

    private val inputRelay = PublishRelay.create<ShareMediaAction>()
    private val eventsRelay = PublishRelay.create<ShareMediaEvent>()

    val input: Consumer<ShareMediaAction> = inputRelay
    val events: Observable<ShareMediaEvent> = eventsRelay
    val state: Observable<ShareMediaState> = inputRelay
        .reduxStore(
            initialState = ShareMediaState(),
            sideEffects = listOf(createWallPost()),
            reducer = ShareMediaState::reduce
        )

    private fun createWallPost(): SharingSideEffect {
        return { actions, state ->
            actions.ofType<OnShareWallPostClicked>().switchMap {
                createWallPost(state().commentText, listOf(state().selectedImage))
                    .subscribeOn(single())
                    .doOnError {
                        Log.i("error", "$it")
                        eventsRelay.accept(ShowUploadingError) }
                    .doOnSuccess { eventsRelay.accept(OnPostSuccessfullyShared(it)) }
                    .flatMapObservable<ShareMediaAction> { Observable.empty() }
                    .onErrorResumeNext(Observable.just(UploadError))
            }
        }
    }
}
