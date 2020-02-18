package com.lounah.vkmc.core.core_vk.domain

import android.util.Log
import com.lounah.vkmc.core.core_vk.business.commands.groups.VKGroupsCommand
import com.lounah.vkmc.core.core_vk.model.Group
import com.vk.api.sdk.VK
import io.reactivex.Single
import java.util.concurrent.TimeUnit
import kotlin.random.Random

typealias Offset = Int
typealias Count = Int

class GetUserGroups : (Offset, Count) -> Single<List<Group>> {

    override fun invoke(offset: Offset, count: Count): Single<List<Group>> {
        return Single.fromCallable { VK.executeSync(VKGroupsCommand(offset, count, reversed = true)) }
    }
}
