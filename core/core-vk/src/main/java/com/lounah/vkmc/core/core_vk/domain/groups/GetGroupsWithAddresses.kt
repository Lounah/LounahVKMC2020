package com.lounah.vkmc.core.core_vk.domain.groups

import com.lounah.vkmc.core.core_vk.business.commands.groups.VKGetAddressesCommand
import com.lounah.vkmc.core.core_vk.business.commands.groups.VKSearchGroupsCommand
import com.lounah.vkmc.core.core_vk.domain.market.CityId
import com.lounah.vkmc.core.core_vk.model.GroupWithAddress
import com.vk.api.sdk.VK
import io.reactivex.Single

class GetGroupsWithAddresses : (CityId, Boolean) -> Single<List<GroupWithAddress>> {

    override fun invoke(cityId: CityId, onlyEvents: Boolean): Single<List<GroupWithAddress>> {
        return Single.create { emitter ->
            val type = if (onlyEvents) "event" else "group"
            val groups = VK.executeSync(VKSearchGroupsCommand(cityId, type = type))
            val result = groups.mapNotNull {
                runCatching {
                    VK.executeSync(VKGetAddressesCommand(it.id)).firstOrNull()?.let { address ->
                        GroupWithAddress(
                            id = it.id,
                            title = it.name,
                            address = address.address,
                            latitude = address.latitude,
                            longitude = address.longitude,
                            photo = it.photo,
                            description = "" // TODO
                        )
                    }
                }.getOrNull()
            }
            emitter.onSuccess(result)
        }
    }
}
