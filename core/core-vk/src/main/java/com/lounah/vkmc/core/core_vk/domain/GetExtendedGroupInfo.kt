package com.lounah.vkmc.core.core_vk.domain

import com.lounah.vkmc.core.core_vk.business.commands.groups.VKExtendedGroupInfoCommand
import com.lounah.vkmc.core.core_vk.model.ExtendedGroup
import com.vk.api.sdk.VK
import io.reactivex.Single

typealias GroupId = Int

class GetExtendedGroupInfo : (GroupId) -> Single<ExtendedGroup> {

    override fun invoke(groupId: GroupId): Single<ExtendedGroup> {
        return Single.fromCallable { VK.executeSync(VKExtendedGroupInfoCommand(groupId)) }
    }
}
