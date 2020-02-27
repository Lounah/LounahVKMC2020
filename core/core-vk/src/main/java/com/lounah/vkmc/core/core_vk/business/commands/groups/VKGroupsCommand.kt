package com.lounah.vkmc.core.core_vk.business.commands.groups

import com.lounah.vkmc.core.core_vk.business.VKApiCommandWrapper
import com.lounah.vkmc.core.core_vk.business.gson
import com.lounah.vkmc.core.core_vk.model.Group
import com.lounah.vkmc.core.core_vk.model.GroupsResponse
import com.vk.api.sdk.VKApiManager
import com.vk.api.sdk.VKApiResponseParser

internal class VKGroupsCommand(
    offset: Int = 0,
    count: Int = 50,
    extended: Boolean = true,
    filter: String = "groups,publics",
    private val reversed: Boolean = false,
    override val method: String = "groups.get",
    override val responseParser: VKApiResponseParser<List<Group>> = VKGroupsResponseParser()
) : VKApiCommandWrapper<List<Group>>() {

    override val arguments: Map<String, String> = mapOf(
        "extended" to if (extended) "1" else "0",
        "filter" to filter,
        "offset" to "$offset",
        "count" to "$count"
    )

    override fun onExecute(manager: VKApiManager): List<Group> {
        val response = super.onExecute(manager)

        return if (reversed) response.reversed() else response
    }

    private class VKGroupsResponseParser : VKApiResponseParser<List<Group>> {
        override fun parse(response: String): List<Group> {
            return gson.fromJson(response, GroupsResponse::class.java).response.items
        }
    }
}
