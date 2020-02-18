package com.lounah.vkmc.feature.feature_unsubscribe.usergroups.ui.viewholders

import com.lounah.vkmc.core.recycler.base.BaseEvents
import com.lounah.vkmc.core.recycler.base.ViewTyped
import com.lounah.vkmc.feature.feature_unsubscribe.usergroups.presentation.UserGroupsAction
import com.lounah.vkmc.feature.feature_unsubscribe.usergroups.presentation.UserGroupsAction.*
import io.reactivex.Observable
import io.reactivex.functions.Consumer

internal class UserGroupsRecyclerEvents(
    private val onRepeatLoadingClicked: Observable<ViewTyped>,
    private val onGroupSelected: Observable<ViewTyped>,
    private val onGroupLongTap: Observable<ViewTyped>,
    private val loadNextPage: Observable<Int>,
    override val input: Consumer<UserGroupsAction>
) : BaseEvents<UserGroupsAction>() {

    override fun bindInternal(): Observable<UserGroupsAction> {
        return Observable.merge(
            onRepeatLoadingClicked.map { OnRetryLoadingClicked },
            onGroupSelected.map { OnGroupSelected(it.uid) },
            onGroupLongTap.map { OnGroupLongTapped(it.uid) },
            loadNextPage.map(::OnPageScrolled)
        )
    }
}
