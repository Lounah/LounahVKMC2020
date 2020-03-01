package com.lounah.vkmc.core.core_vk.business.commands.groups

import androidx.annotation.Keep
import com.lounah.vkmc.core.core_vk.business.VKApiCommandWrapper
import com.lounah.vkmc.core.core_vk.business.gson
import com.vk.api.sdk.VKApiResponseParser

internal class VKGetGroupLastPostTimeCommand(
    groupId: String,
    override val method: String = "wall.get",
    override val arguments: Map<String, String> = mapOf("owner_id" to "-$groupId", "count" to "1"),
    override val responseParser: VKApiResponseParser<Long> = VKGroupPostsReponseParser()
) : VKApiCommandWrapper<Long>() {

    private class VKGroupPostsReponseParser: VKApiResponseParser<Long> {
        override fun parse(response: String): Long {
            return gson.fromJson(response, VKGroupWallResponse::class.java).response.items.first()
                .date
        }
    }
}

@Keep
private data class VKGroupWallResponse(val response: VKGroupWall) {
    @Keep
    data class VKGroupWall(val items: List<GroupWallItem>)
    @Keep
    data class GroupWallItem(val date: Long)
}
