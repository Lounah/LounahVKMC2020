package com.lounah.vkmc.core.core_vk.domain

import com.lounah.vkmc.core.core_vk.business.commands.groups.VKGroupsCommand
import com.lounah.vkmc.core.core_vk.model.Group
import com.vk.api.sdk.VK
import io.reactivex.Single

typealias Offset = Int
typealias Count = Int

class GetUserGroups : (Offset, Count) -> Single<List<Group>> {

    override fun invoke(offset: Offset, count: Count): Single<List<Group>> {
        return Single.fromCallable { VK.executeSync(VKGroupsCommand(offset, count)) }
    }
}
