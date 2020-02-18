package com.lounah.vkmc.feature.feature_unsubscribe.groupdetails.presentation

import com.lounah.vkmc.feature.feature_unsubscribe.groupdetails.ui.ExtendedGroupUi


sealed class GroupDetailsAction {
    class OnGroupLoaded(val group: ExtendedGroupUi) : GroupDetailsAction()
    object OnGoToGroupClicked : GroupDetailsAction()
    object OnLoadingError : GroupDetailsAction()
    object OnRetryLoadClicked : GroupDetailsAction()
}