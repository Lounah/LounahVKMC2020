package com.lounah.vkmc.core.core_vk.domain.groups

import com.lounah.vkmc.core.core_vk.business.commands.groups.VKLeaveGroupsCommand
import com.vk.api.sdk.VK
import io.reactivex.Single

class LeaveGroups : (List<String>) -> Single<Boolean> {

    override fun invoke(groupIds: List<String>): Single<Boolean> {
        return Single.create { emitter ->
            val result = VK.executeSync(VKLeaveGroupsCommand(groupIds))
            if (result) emitter.onSuccess(result) else emitter.onError(Throwable())
        }
    }
}
