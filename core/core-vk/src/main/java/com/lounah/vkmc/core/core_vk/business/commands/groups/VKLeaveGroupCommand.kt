package com.lounah.vkmc.core.core_vk.business.commands.groups

import androidx.annotation.Keep
import com.google.gson.Gson
import com.lounah.vkmc.core.core_vk.business.VKApiCommandWrapper
import com.vk.api.sdk.VKApiManager
import com.vk.api.sdk.VKApiResponseParser
import com.vk.api.sdk.internal.ApiCommand

internal class VKLeaveGroupCommand(
    groupId: String,
    override val arguments: Map<String, String> = mapOf("group_id" to "$groupId"),
    override val method: String = "groups.leave",
    override val responseParser: VKApiResponseParser<LeaveGroupResponse> = VKLeaveGroupResponseParser()
) : VKApiCommandWrapper<LeaveGroupResponse>() {

    private class VKLeaveGroupResponseParser(
        private val gson: Gson = Gson()
    ) : VKApiResponseParser<LeaveGroupResponse> {
        override fun parse(response: String): LeaveGroupResponse {
            return gson.fromJson(response, LeaveGroupResponse::class.java)
        }
    }
}

internal class VKLeaveGroupsCommand(
    private val groupIds: List<String>
) : ApiCommand<Boolean>() {

    override fun onExecute(manager: VKApiManager): Boolean {
        return groupIds.map { VKLeaveGroupCommand(it).execute(manager) }
            .all(LeaveGroupResponse::isSuccess)
    }
}

@Keep
internal data class LeaveGroupResponse(
    val response: Int
) {
    val isSuccess: Boolean
        get() = response == 1
}
