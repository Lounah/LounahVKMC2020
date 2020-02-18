package com.lounah.vkmc.feature.feature_unsubscribe.usergroups.presentation

import com.lounah.vkmc.core.extensions.asType
import com.lounah.vkmc.core.recycler.base.ViewTyped
import com.lounah.vkmc.core.recycler.base.items.ErrorView
import com.lounah.vkmc.core.recycler.base.items.ProgressItem
import com.lounah.vkmc.core.recycler.diff.DefaultDiffCalculator
import com.lounah.vkmc.core.recycler.diff.DiffResult
import com.lounah.vkmc.feature.feature_unsubscribe.usergroups.presentation.UserGroupsAction.*
import com.lounah.vkmc.feature.feature_unsubscribe.usergroups.ui.recycler.GroupsHeaderUi
import com.lounah.vkmc.core.recycler.paging.core.PagedErrorUi
import com.lounah.vkmc.core.recycler.paging.core.PagedProgressUi
import com.lounah.vkmc.feature.feature_unsubscribe.usergroups.ui.recycler.UserGroupUi

data class UserGroupsState(
    val userGroups: List<ViewTyped> = emptyList(),
    val errorView: ViewTyped = ErrorView(),
    val pagedProgress: ViewTyped = PagedProgressUi(),
    val pagedError: ViewTyped = PagedErrorUi(),
    val pageOffset: Int = 0,
    val groupsDeletionLoading: Boolean = false
)

internal fun UserGroupsState.reduce(action: UserGroupsAction): UserGroupsState {
    return when (action) {
        is OnPageScrolled -> copy(pageOffset = action.offset)
        is OnGroupsLoaded -> {
            val header = if (pageOffset == 0) listOf(GroupsHeaderUi) else emptyList()
            val newItems =
                header + (userGroups - ProgressItem - errorView - pagedProgress - pagedError) + action.groups
            copy(userGroups = newItems)
        }
        is OnLoadingStarted -> {
            val newItems = if (pageOffset == 0) listOf(ProgressItem) else {
                (userGroups - errorView - pagedError - ProgressItem - pagedProgress) + pagedProgress
            }.toList()
            copy(userGroups = newItems)
        }
        is OnLoadingError -> {
            val newItems = when (pageOffset) {
                0 -> listOf(errorView)
                else -> userGroups - pagedError + pagedError - pagedProgress
            }
            copy(userGroups = newItems)
        }
        is OnGroupSelected -> {
            val groupId = action.uid
            val groups = userGroups.toMutableList().map {
                if (it.uid == groupId)
                    (it as UserGroupUi).copy(isSelected = !it.isSelected)
                else it
            }
            copy(userGroups = groups)
        }
        else -> this
    }
}

