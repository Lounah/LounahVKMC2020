package com.lounah.vkmc.feature.feature_map.details.presentation

import com.lounah.vkmc.feature.feature_map.details.ui.GroupOrEventUi

sealed class GroupOrEventAction {
    class OnGroupLoaded(val group: GroupOrEventUi) : GroupOrEventAction()
    object OnLoadingError : GroupOrEventAction()
    object OnRetryLoadClicked : GroupOrEventAction()
}
