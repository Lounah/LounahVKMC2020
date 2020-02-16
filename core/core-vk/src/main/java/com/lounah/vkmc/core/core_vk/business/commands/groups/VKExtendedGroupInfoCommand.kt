package com.lounah.vkmc.core.core_vk.business.commands.groups

import com.lounah.vkmc.core.core_vk.business.VKApiCommandWrapper
import com.lounah.vkmc.core.core_vk.model.ExtendedGroup
import com.lounah.vkmc.core.core_vk.model.GroupById
import com.vk.api.sdk.VKApiManager
import com.vk.api.sdk.internal.ApiCommand

internal class VKExtendedGroupInfoCommand(
    private val groupId: Int,
    private val getGroupById: VKApiCommandWrapper<GroupById> = VKGetGroupByIdCommand(groupId),
    private val getGroupFriends: VKApiCommandWrapper<Int> = VKGetGroupFriendsCountCommand(groupId),
    private val getGroupLastPostTime: VKApiCommandWrapper<Long> = VKGetGroupLastPostTimeCommand(groupId)
) : ApiCommand<ExtendedGroup>() {

    override fun onExecute(manager: VKApiManager): ExtendedGroup {
        val groupById = getGroupById.execute(manager)
        val groupFriends = getGroupFriends.execute(manager)
        val lastPostTime = getGroupLastPostTime.execute(manager)

        return ExtendedGroup(
            groupId,
            groupById.name,
            groupById.photo,
            groupById.subscribers,
            groupFriends,
            groupById.description,
            lastPostTime
        )
    }
}