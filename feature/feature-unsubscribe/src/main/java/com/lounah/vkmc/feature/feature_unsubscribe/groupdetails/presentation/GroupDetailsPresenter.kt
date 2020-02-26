package com.lounah.vkmc.feature.feature_unsubscribe.groupdetails.presentation

import com.freeletics.rxredux.SideEffect
import com.freeletics.rxredux.reduxStore
import com.jakewharton.rxrelay2.PublishRelay
import com.lounah.vkmc.core.core_vk.domain.GroupId
import com.lounah.vkmc.core.core_vk.model.ExtendedGroup
import com.lounah.vkmc.feature.feature_unsubscribe.groupdetails.presentation.GroupDetailsAction.*
import com.lounah.vkmc.feature.feature_unsubscribe.groupdetails.presentation.GroupDetailsEvent.OpenGroupDetails
import com.lounah.vkmc.feature.feature_unsubscribe.groupdetails.ui.ExtendedGroupUi
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers.mainThread
import io.reactivex.functions.Consumer
import io.reactivex.rxkotlin.ofType
import io.reactivex.schedulers.Schedulers.single
import java.util.concurrent.TimeUnit.MILLISECONDS


private typealias GroupDetailsSideEffect = SideEffect<GroupDetailsState, GroupDetailsAction>

class GroupDetailsPresenterCreator(
    private val getExtendedGroupInfo: (GroupId) -> Single<ExtendedGroup>,
    private val groupDetailsMapper: (ExtendedGroup) -> ExtendedGroupUi
) : (GroupId) -> GroupDetailsPresenter {
    override fun invoke(groupId: GroupId): GroupDetailsPresenter {
        return GroupDetailsPresenter(getExtendedGroupInfo, groupDetailsMapper, groupId)
    }
}

class GroupDetailsPresenter(
    private val getExtendedGroupInfo: (GroupId) -> Single<ExtendedGroup>,
    private val groupDetailsMapper: (ExtendedGroup) -> ExtendedGroupUi,
    private val groupId: Int
) {

    private val inputRelay = PublishRelay.create<GroupDetailsAction>()
    private val eventsRelay = PublishRelay.create<GroupDetailsEvent>()

    val input: Consumer<GroupDetailsAction> = inputRelay
    val events: Observable<GroupDetailsEvent> = eventsRelay
    val state: Observable<GroupDetailsState> = inputRelay
        .reduxStore(
            initialState = GroupDetailsState(groupId),
            sideEffects = listOf(loadGroup(), retryLoadGroup(), onGroupOpenClicked()),
            reducer = GroupDetailsState::reduce
        ).distinctUntilChanged()

    private fun loadGroup(): GroupDetailsSideEffect {
        return { _, _ -> loadGroupDetails() }
    }

    private fun onGroupOpenClicked(): GroupDetailsSideEffect {
        return { actions, state ->
            actions.ofType<OnGoToGroupClicked>().flatMap {
                state().groupDetails?.let {
                    eventsRelay.accept(OpenGroupDetails(it.screenName))
                }
                Observable.empty<GroupDetailsAction>()
            }
        }
    }

    private fun retryLoadGroup(): GroupDetailsSideEffect {
        return { actions, _ ->
            actions.ofType<OnRetryLoadClicked>().switchMap { loadGroupDetails() }
        }
    }

    private fun loadGroupDetails(): Observable<GroupDetailsAction> {
        return Observable.defer {
            getExtendedGroupInfo(groupId)
                .subscribeOn(single())
                .observeOn(mainThread())
                .delay(500, MILLISECONDS)
                .flatMapObservable<GroupDetailsAction> {
                    Observable.just(OnGroupLoaded(groupDetailsMapper(it)))
                }
                .onErrorReturnItem(OnLoadingError)
        }
    }
}
