package com.lounah.vkmc.feature.feature_unsubscribe.di

import com.lounah.vkmc.core.core_vk.domain.groups.Count
import com.lounah.vkmc.core.core_vk.domain.groups.GetUserGroups
import com.lounah.vkmc.core.core_vk.domain.groups.LeaveGroups
import com.lounah.vkmc.core.core_vk.domain.groups.Offset
import com.lounah.vkmc.core.core_vk.model.Group
import com.lounah.vkmc.feature.feature_unsubscribe.usergroups.domain.UserGroupsMapper
import com.lounah.vkmc.feature.feature_unsubscribe.usergroups.presentation.UserGroupsPresenter
import com.lounah.vkmc.feature.feature_unsubscribe.usergroups.ui.recycler.UserGroupUi
import io.reactivex.Single

interface UserGroupsComponent {
    val presenter: UserGroupsPresenter
}

fun UserGroupsComponent(): UserGroupsComponent = object : UserGroupsComponent {
    private val getUserGroups: (Offset, Count) -> Single<List<Group>> =
        GetUserGroups()
    private val userGroupsMapper: (List<Group>) -> List<UserGroupUi> = UserGroupsMapper()
    private val leaveGroups: (List<String>) -> Single<Boolean> =
        LeaveGroups()

    override val presenter: UserGroupsPresenter =
        UserGroupsPresenter(getUserGroups, userGroupsMapper, leaveGroups)
}
