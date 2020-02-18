package com.lounah.vkmc.feature.feature_unsubscribe.di

import com.lounah.vkmc.core.core_vk.domain.Count
import com.lounah.vkmc.core.core_vk.domain.GetUserGroups
import com.lounah.vkmc.core.core_vk.domain.LeaveGroups
import com.lounah.vkmc.core.core_vk.domain.Offset
import com.lounah.vkmc.core.core_vk.model.Group
import com.lounah.vkmc.feature.feature_unsubscribe.usergroups.domain.UserGroupsMapper
import com.lounah.vkmc.feature.feature_unsubscribe.usergroups.presentation.UserGroupsPresenter
import com.lounah.vkmc.feature.feature_unsubscribe.usergroups.ui.viewholders.UserGroupUi
import io.reactivex.Completable
import io.reactivex.Single

interface UserGroupsComponent {
    val presenter: UserGroupsPresenter
}

fun UserGroupsComponent(): UserGroupsComponent = object : UserGroupsComponent {
    private val getUserGroups: (Offset, Count) -> Single<List<Group>> = GetUserGroups()
    private val userGroupsMapper: (List<Group>) -> List<UserGroupUi> = UserGroupsMapper()
    private val leaveGroups: (List<Int>) -> Completable = LeaveGroups()

    override val presenter: UserGroupsPresenter =
        UserGroupsPresenter(getUserGroups, userGroupsMapper, leaveGroups)
}