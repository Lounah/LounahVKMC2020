package com.lounah.vkmc.feature.feature_map.details.presentation

import com.freeletics.rxredux.SideEffect
import com.freeletics.rxredux.reduxStore
import com.jakewharton.rxrelay2.PublishRelay
import com.lounah.vkmc.core.core_vk.domain.groups.GroupId
import com.lounah.vkmc.core.core_vk.model.ExtendedGroup
import com.lounah.vkmc.feature.feature_map.details.domain.ExtendedGroupMapper
import com.lounah.vkmc.feature.feature_map.details.presentation.GroupOrEventAction.*
import com.lounah.vkmc.feature.feature_map.details.ui.GroupOrEventUi
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.Consumer
import io.reactivex.rxkotlin.ofType
import io.reactivex.schedulers.Schedulers

private typealias GroupDetailsSideEffect = SideEffect<GroupOrEventState, GroupOrEventAction>

class GroupOrEventDetailsPresenterFactory(
    private val getExtendedGroupInfo: (GroupId) -> Single<ExtendedGroup>
) : (GroupId, String) -> GroupOrEventDetailsPresenter {
    override fun invoke(groupId: GroupId, groupAddress: String): GroupOrEventDetailsPresenter {
        return GroupOrEventDetailsPresenter(getExtendedGroupInfo, ExtendedGroupMapper(groupAddress), groupId)
    }
}

class GroupOrEventDetailsPresenter(
    private val getExtendedGroupInfo: (GroupId) -> Single<ExtendedGroup>,
    private val groupDetailsMapper: (ExtendedGroup) -> GroupOrEventUi,
    private val groupId: String
) {

    private val inputRelay = PublishRelay.create<GroupOrEventAction>()

    val input: Consumer<GroupOrEventAction> = inputRelay
    val state: Observable<GroupOrEventState> = inputRelay
        .reduxStore(
            initialState = GroupOrEventState(groupId),
            sideEffects = listOf(loadGroup(), retryLoadGroup()),
            reducer = GroupOrEventState::reduce
        ).distinctUntilChanged()

    private fun loadGroup(): GroupDetailsSideEffect {
        return { _, _ -> loadGroupDetails() }
    }

    private fun retryLoadGroup(): GroupDetailsSideEffect {
        return { actions, _ ->
            actions.ofType<OnRetryLoadClicked>().switchMap { loadGroupDetails() }
        }
    }

    private fun loadGroupDetails(): Observable<GroupOrEventAction> {
        return Observable.defer {
            getExtendedGroupInfo(groupId)
                .subscribeOn(Schedulers.single())
                .observeOn(AndroidSchedulers.mainThread())
                .flatMapObservable<GroupOrEventAction> {
                    Observable.just(OnGroupLoaded(groupDetailsMapper(it)))
                }
                .onErrorReturnItem(OnLoadingError)
        }
    }
}
