package com.lounah.vkmc.feature.feature_unsubscribe.usergroups.presentation

sealed class UserGroupsEvent {
    class OpenExtraGroupInfoDialog(val groupId: String) : UserGroupsEvent()
    object ShowGroupsLeaveError : UserGroupsEvent()
}
