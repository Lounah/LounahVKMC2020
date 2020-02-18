package com.lounah.vkmc.feature.feature_unsubscribe.usergroups.presentation

import com.lounah.vkmc.core.recycler.base.ReduxAction
import com.lounah.vkmc.core.recycler.base.ViewTyped

sealed class UserGroupsAction : ReduxAction {
    object InitialLoading : UserGroupsAction()
    object OnLoadingError : UserGroupsAction()
    object OnLoadingStarted : UserGroupsAction()
    object OnLeaveGroupsClicked : UserGroupsAction()
    object OnRetryLoadingClicked : UserGroupsAction()
    class OnGroupSelected(val uid: String) : UserGroupsAction()
    class OnGroupLongTapped(val uid: String) : UserGroupsAction()
    class OnPageScrolled(val offset: Int) : UserGroupsAction()
    class OnGroupsLoaded(val groups: List<ViewTyped>) : UserGroupsAction()
}