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

    val cache = mutableListOf<Group>()
    val rnd = Random(1337)

    override fun invoke(offset: Offset, count: Count): Single<List<Group>> {
        return Single.fromCallable {
            Log.i("load", "LOADDDD")
            val res = VK.executeSync(VKGroupsCommand(offset, count, reversed = true))
            cache.addAll(res)
            if (res.isEmpty()) return@fromCallable cache.map {
                val d = rnd.nextInt(1000000)
                it.copy(id = d, name = d.toString())
            } else return@fromCallable res
        }.delay(2, TimeUnit.SECONDS)
    }
}
