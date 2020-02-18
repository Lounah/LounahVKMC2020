package com.lounah.vkmc.feature.feature_unsubscribe.usergroups.presentation

import com.lounah.vkmc.core.recycler.base.ViewTyped
import com.lounah.vkmc.core.recycler.base.items.ErrorView
import com.lounah.vkmc.core.recycler.base.items.ProgressItem
import com.lounah.vkmc.core.recycler.diff.DefaultDiffCalculator
import com.lounah.vkmc.core.recycler.diff.DiffResult
import com.lounah.vkmc.feature.feature_unsubscribe.usergroups.presentation.UserGroupsAction.*
import com.lounah.vkmc.feature.feature_unsubscribe.usergroups.ui.viewholders.GroupsHeaderUi
import com.lounah.vkmc.feature.feature_unsubscribe.usergroups.ui.viewholders.PagedErrorUi
import com.lounah.vkmc.feature.feature_unsubscribe.usergroups.ui.viewholders.PagedProgressUi

data class UserGroupsState(
    val userGroups: List<ViewTyped> = emptyList(),
    val diff: DiffResult<ViewTyped> = DiffResult.EmptyDiffResult,
    val errorView: ViewTyped = ErrorView(),
    val pagedProgress: ViewTyped = PagedProgressUi(),
    val pagedError: ViewTyped = PagedErrorUi(),
    val selectedGroups: List<Int> = emptyList(),
    val pageOffset: Int = 0,
    val shouldUpdate: Boolean = true,
    val groupsDeletionLoading: Boolean = false
)

internal fun UserGroupsState.reduce(action: UserGroupsAction): UserGroupsState {
    return when (action) {
        is OnPageScrolled -> copy(pageOffset = action.offset)
        is OnGroupsLoaded -> {
            val header = if (pageOffset == 0) listOf(GroupsHeaderUi) else emptyList()
            val newItems =
                header + (userGroups - ProgressItem - errorView - pagedProgress - pagedError) + action.groups
            val diff = DefaultDiffCalculator(userGroups.toList(), newItems.toList())
            copy(diff = diff, userGroups = diff.items, shouldUpdate = true)
        }
        is OnLoadingStarted -> {
            val newItems = if (pageOffset == 0) listOf(ProgressItem) else {
                (userGroups - errorView - pagedError - ProgressItem - pagedProgress) + pagedProgress
            }.toList()
            val diff = DefaultDiffCalculator(userGroups, newItems)
            copy(diff = diff, userGroups = diff.items.toList(), shouldUpdate = true)
        }
        is OnLoadingError -> {
            val newItems = when (pageOffset) {
                0 -> listOf(errorView)
                else -> userGroups + pagedError - pagedProgress
            }
            val diff = DefaultDiffCalculator(userGroups, newItems.toList())
            copy(diff = diff, userGroups = diff.items.toList(), shouldUpdate = true)
        }
        is OnGroupSelected -> {
            val groupId = action.uid.toInt()
            val newGroups = if (selectedGroups.contains(groupId)) {
                selectedGroups - groupId
            } else {
                selectedGroups + groupId
            }
            copy(selectedGroups = newGroups, shouldUpdate = false)
        }
        else -> this
    }
}

