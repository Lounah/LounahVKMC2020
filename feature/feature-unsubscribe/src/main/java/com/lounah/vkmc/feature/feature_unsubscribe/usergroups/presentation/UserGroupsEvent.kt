package com.lounah.vkmc.feature.feature_unsubscribe.usergroups.presentation

sealed class UserGroupsEvent {
    class OpenExtraGroupInfoDialog(val groupId: Int) : UserGroupsEvent()
    object ShowGroupsLeaveError : UserGroupsEvent()
}
