package com.lounah.vkmc.feature.feature_map.details.presentation

import com.lounah.vkmc.feature.feature_map.details.presentation.GroupOrEventAction.*
import com.lounah.vkmc.feature.feature_map.details.ui.GroupOrEventUi

data class GroupOrEventState(
    val groupId: String,
    val fullScreenLoading: Boolean = true,
    val fullScreenError: Boolean = false,
    val groupDetails: GroupOrEventUi? = null
)

internal fun GroupOrEventState.reduce(action: GroupOrEventAction): GroupOrEventState {
    return when (action) {
        is OnLoadingError -> copy(fullScreenLoading = false, fullScreenError = true)
        is OnRetryLoadClicked -> copy(fullScreenLoading = true, fullScreenError = false)
        is OnGroupLoaded -> copy(
            fullScreenLoading = false,
            fullScreenError = false,
            groupDetails = action.group
        )
    }
}
