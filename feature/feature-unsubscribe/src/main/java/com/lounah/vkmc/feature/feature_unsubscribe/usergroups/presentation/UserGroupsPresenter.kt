package com.lounah.vkmc.feature.feature_unsubscribe.usergroups.presentation

import android.util.Log
import com.freeletics.rxredux.SideEffect
import com.freeletics.rxredux.reduxStore
import com.jakewharton.rxrelay2.PublishRelay
import com.lounah.vkmc.core.core_vk.domain.Count
import com.lounah.vkmc.core.core_vk.domain.Offset
import com.lounah.vkmc.core.core_vk.model.Group
import com.lounah.vkmc.core.extensions.asType
import com.lounah.vkmc.core.recycler.paging.DEFAULT_PAGE_SIZE
import com.lounah.vkmc.feature.feature_unsubscribe.usergroups.presentation.UserGroupsAction.*
import com.lounah.vkmc.feature.feature_unsubscribe.usergroups.presentation.UserGroupsEvent.*
import com.lounah.vkmc.feature.feature_unsubscribe.usergroups.ui.recycler.UserGroupUi
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.android.schedulers.AndroidSchedulers.mainThread
import io.reactivex.functions.Consumer
import io.reactivex.rxkotlin.ofType
import io.reactivex.schedulers.Schedulers.io
import io.reactivex.schedulers.Schedulers.single

private typealias UserGroupsSideEffect = SideEffect<UserGroupsState, UserGroupsAction>

class UserGroupsPresenter(
    private val getUserGroups: (Offset, Count) -> Single<List<Group>>,
    private val userGroupsMapper: (List<Group>) -> List<UserGroupUi>,
    private val leaveGroups: (List<String>) -> Single<Boolean>
) {

    private val inputRelay = PublishRelay.create<UserGroupsAction>()
    private val eventsRelay = PublishRelay.create<UserGroupsEvent>()

    val input: Consumer<UserGroupsAction> = inputRelay
    val events: Observable<UserGroupsEvent> = eventsRelay
    val state: Observable<UserGroupsState> = inputRelay
        .reduxStore(
            initialState = UserGroupsState(),
            sideEffects = listOf(
                loadPagedGroups(),
                initialLoading(),
                repeatLoadGroups(),
                handleGroupLongTap(),
                handleLeaveGroupsClick()
            ),
            reducer = UserGroupsState::reduce
        ).distinctUntilChanged()

    private fun handleLeaveGroupsClick(): UserGroupsSideEffect {
        return { actions, state ->
            actions.ofType<OnLeaveGroupsClicked>().flatMap {
                leaveGroups(state().selectedGroupsIds())
                    .subscribeOn(io())
                    .observeOn(mainThread())
                    .toObservable()
                    .map<UserGroupsAction> { OnGroupsLeft }
                    .doOnError { eventsRelay.accept(ShowGroupsLeaveError) }
                    .onErrorReturnItem(OnLeftGroupsError)
            }
        }
    }

    private fun initialLoading(): UserGroupsSideEffect {
        return { _, _ -> loadGroupsPaged(0) }
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

    private fun loadPagedGroups(): UserGroupsSideEffect {
        return { actions, _ ->
            actions.ofType<OnPageScrolled>().flatMap { loadGroupsPaged(it.offset) }
        }
    }

    private fun repeatLoadGroups(): UserGroupsSideEffect {
        return { actions, state ->
            actions.ofType<OnRetryLoadingClicked>().flatMap { loadGroupsPaged(state().pageOffset) }
        }
    }

    private fun loadGroupsPaged(page: Int): Observable<UserGroupsAction> {
        return getUserGroups(page, DEFAULT_PAGE_SIZE)
            .subscribeOn(single())
            .observeOn(mainThread())
            .toObservable()
            .map<UserGroupsAction> { OnGroupsLoaded(userGroupsMapper(it)) }
            .onErrorReturnItem(OnLoadingError)
            .startWith(OnLoadingStarted)
    }
}
