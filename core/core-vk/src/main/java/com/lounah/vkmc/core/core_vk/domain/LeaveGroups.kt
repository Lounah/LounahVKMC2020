package com.lounah.vkmc.core.core_vk.domain

import com.lounah.vkmc.core.core_vk.business.commands.groups.VKLeaveGroupsCommand
import com.vk.api.sdk.VK
import io.reactivex.Completable

class LeaveGroups : (List<Int>) -> Completable {

    override fun invoke(groupIds: List<Int>): Completable {
        return Completable.create { emitter ->
            val result = VK.executeSync(VKLeaveGroupsCommand(groupIds))
            if (result) emitter.onComplete() else emitter.onError(Throwable())
        }
    }
}
