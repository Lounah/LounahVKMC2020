package com.lounah.vkmc.core.core_vk.business.commands.groups

import com.google.gson.Gson
import com.lounah.vkmc.core.core_vk.business.VKApiCommandWrapper
import com.vk.api.sdk.VKApiResponseParser

internal class VKGetGroupLastPostTimeCommand(
    groupId: Int,
    override val method: String = "wall.get",
    override val arguments: Map<String, String> = mapOf("owner_id" to "-$groupId", "count" to "1"),
    override val responseParser: VKApiResponseParser<Long> = VKGroupPostsReponseParser()
) : VKApiCommandWrapper<Long>() {

    private class VKGroupPostsReponseParser(
        private val gson: Gson = Gson()
    ) : VKApiResponseParser<Long> {
        override fun parse(response: String): Long {
            return gson.fromJson(response, VKGroupWallResponse::class.java).response.items.first()
                .date
        }
    }
}

private data class VKGroupWallResponse(val response: VKGroupWall) {
    data class VKGroupWall(val items: List<GroupWallItem>)
    data class GroupWallItem(val date: Long)
}