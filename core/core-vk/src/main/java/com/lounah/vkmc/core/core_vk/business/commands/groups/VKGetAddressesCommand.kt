package com.lounah.vkmc.core.core_vk.business.commands.groups

import com.lounah.vkmc.core.core_vk.business.VKApiCommandWrapper
import com.lounah.vkmc.core.core_vk.business.gson
import com.lounah.vkmc.core.core_vk.model.GroupAddress
import com.lounah.vkmc.core.core_vk.model.GroupAddressesResponse
import com.vk.api.sdk.VKApiResponseParser

internal class VKGetAddressesCommand(
    groupId: String,
    override val method: String = "groups.getAddresses",
    override val responseParser: VKApiResponseParser<List<GroupAddress>> = VKGetAddressesResponseParser()
) : VKApiCommandWrapper<List<GroupAddress>>() {

    override val arguments: Map<String, String> = mapOf(
        "group_id" to groupId,
        "fields" to "address,latitude,longitude"
    )

    private class VKGetAddressesResponseParser : VKApiResponseParser<List<GroupAddress>> {

        override fun parse(response: String?): List<GroupAddress> {
            return gson.fromJson(response, GroupAddressesResponse::class.java).response.items
        }
    }
}
