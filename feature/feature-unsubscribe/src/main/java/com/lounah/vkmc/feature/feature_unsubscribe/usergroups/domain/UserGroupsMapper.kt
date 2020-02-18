package com.lounah.vkmc.feature.feature_unsubscribe.usergroups.domain

import com.lounah.vkmc.core.core_vk.model.Group
import com.lounah.vkmc.feature.feature_unsubscribe.usergroups.ui.recycler.UserGroupUi

internal class UserGroupsMapper : (List<Group>) -> List<UserGroupUi> {

    override fun invoke(users: List<Group>): List<UserGroupUi> {
        return users.map {
            UserGroupUi(it.id.toString(), it.photo, it.name)
        }
    }
}
