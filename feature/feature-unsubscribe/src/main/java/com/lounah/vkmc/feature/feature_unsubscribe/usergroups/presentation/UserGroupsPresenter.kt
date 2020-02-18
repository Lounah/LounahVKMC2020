package com.lounah.vkmc.feature.feature_unsubscribe.usergroups.presentation

import com.freeletics.rxredux.SideEffect
import com.freeletics.rxredux.reduxStore
import com.jakewharton.rxrelay2.PublishRelay
import com.lounah.vkmc.core.core_vk.domain.Count
import com.lounah.vkmc.core.core_vk.domain.Offset
import com.lounah.vkmc.core.core_vk.model.Group
import com.lounah.vkmc.core.recycler.paging.DEFAULT_PAGE_SIZE
import com.lounah.vkmc.feature.feature_unsubscribe.usergroups.presentation.UserGroupsAction.*
import com.lounah.vkmc.feature.feature_unsubscribe.usergroups.presentation.UserGroupsEvent.*
import com.lounah.vkmc.feature.feature_unsubscribe.usergroups.ui.viewholders.UserGroupUi
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.Consumer
import io.reactivex.rxkotlin.ofType
import io.reactivex.schedulers.Schedulers.single

private typealias UserGroupsSideEffect = SideEffect<UserGroupsState, UserGroupsAction>

class UserGroupsPresenter(
    private val getUserGroups: (Offset, Count) -> Single<List<Group>>,
    private val userGroupsMapper: (List<Group>) -> List<UserGroupUi>,
    private val leaveGroups: (List<Int>) -> Completable
) {

    private val inputRelay = PublishRelay.create<UserGroupsAction>()
    private val eventsRelay = PublishRelay.create<UserGroupsEvent>()

    val input: Consumer<UserGroupsAction> = inputRelay
    val events: Observable<UserGroupsEvent> = eventsRelay
    val state: Observable<UserGroupsState> = inputRelay
        .reduxStore(
            initialState = UserGroupsState(),
            sideEffects = listOf(loadGroups(), handleGroupLongTap(), handleLeaveGroupsClick()),
            reducer = UserGroupsState::reduce
        ).distinctUntilChanged()

    private fun handleLeaveGroupsClick(): UserGroupsSideEffect {
        return { actions, state ->
            actions.ofType<OnLeaveGroupsClicked>().switchMap {
                leaveGroups(state().selectedGroups)
                    .doOnComplete { eventsRelay.accept(ShowGroupsLeaveSuccess) }
                    .doOnError { eventsRelay.accept(ShowGroupsLeaveError) }
                    .onErrorComplete()
                    .toObservable<UserGroupsAction>()
            }
        }
    }

    private fun handleGroupLongTap(): UserGroupsSideEffect {
        return { actions, _ ->
            actions.ofType<OnGroupLongTapped>().flatMap {
                val groupId = it.uid.toInt()
                eventsRelay.accept(OpenExtraGroupInfoDialog(groupId))
                Observable.empty<UserGroupsAction>()
            }
        }
    }

    private fun loadGroups(): UserGroupsSideEffect {
        return { actions, state ->
            actions.ofType<OnPageScrolled>().flatMap {
                //            actions.filter { it is OnPageScrolled || it is OnRetryLoadingClicked || it is InitialLoading }
//                .flatMap {
                getUserGroups(state().pageOffset, DEFAULT_PAGE_SIZE)
                    .subscribeOn(single())
                    .observeOn(AndroidSchedulers.mainThread())
                    .toObservable()
                    .map<UserGroupsAction> { OnGroupsLoaded(userGroupsMapper(it)) }
                    .onErrorReturnItem(OnLoadingError)
                    .startWith(OnLoadingStarted)
//                }
            }
        }
    }
}