package com.lounah.vkmc.core.core_vk.business.commands.groups

import androidx.annotation.Keep
import com.lounah.vkmc.core.core_vk.business.VKApiCommandWrapper
import com.lounah.vkmc.core.core_vk.business.gson
import com.vk.api.sdk.VKApiResponseParser

internal class VKGetGroupFriendsCountCommand(
    groupId: String,
    override val method: String = "groups.getMembers",
    override val responseParser: VKApiResponseParser<Int> = VkFriendsCountParser()
) : VKApiCommandWrapper<Int>() {

    override val arguments: Map<String, String> = mapOf(
        "filter" to "friends",
        "group_id" to groupId
    )

    private class VkFriendsCountParser : VKApiResponseParser<Int> {
        override fun parse(response: String): Int {
            return gson.fromJson(response, GroupMembersResponse::class.java).response.items.size
        }
    }
}

@Keep
private data class GroupMembersResponse(
    val response: GroupMembers
) {

    @Keep
    data class GroupMembers(val items: List<Int>)
}
