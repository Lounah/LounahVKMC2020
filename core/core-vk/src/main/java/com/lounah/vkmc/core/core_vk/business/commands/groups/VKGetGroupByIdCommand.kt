package com.lounah.vkmc.core.core_vk.business.commands.groups

import com.lounah.vkmc.core.core_vk.business.VKApiCommandWrapper
import com.lounah.vkmc.core.core_vk.business.gson
import com.lounah.vkmc.core.core_vk.model.GroupById
import com.lounah.vkmc.core.core_vk.model.GroupByIdResponse
import com.vk.api.sdk.VKApiResponseParser

internal class VKGetGroupByIdCommand(
    groupId: String,
    override val method: String = "groups.getById",
    override val responseParser: VKApiResponseParser<GroupById> = VKGroupByIdResponseParser()
) : VKApiCommandWrapper<GroupById>() {

    override val arguments: Map<String, String> = mapOf(
        "group_id" to groupId,
        "fields" to "description,members_count"
    )

    private class VKGroupByIdResponseParser : VKApiResponseParser<GroupById> {
        override fun parse(response: String): GroupById {
            return gson.fromJson(response, GroupByIdResponse::class.java).response.first()
        }
    }
}
