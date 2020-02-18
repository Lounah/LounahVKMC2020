package com.lounah.vkmc.feature.feature_unsubscribe.groupdetails.presentation

import com.lounah.vkmc.feature.feature_unsubscribe.groupdetails.presentation.GroupDetailsAction.*
import com.lounah.vkmc.feature.feature_unsubscribe.groupdetails.ui.ExtendedGroupUi

data class GroupDetailsState(
    val groupId: Int,
    val fullScreenLoading: Boolean = true,
    val fullScreenError: Boolean = false,
    val groupDetails: ExtendedGroupUi? = null
)

internal fun GroupDetailsState.reduce(action: GroupDetailsAction): GroupDetailsState {
    return when (action) {
        is OnLoadingError -> copy(fullScreenLoading = false, fullScreenError = true)
        is OnRetryLoadClicked -> copy(fullScreenLoading = true, fullScreenError = false)
        is OnGroupLoaded -> copy(
            fullScreenLoading = false,
            fullScreenError = false,
            groupDetails = action.group
        )
        else -> this
    }
}