package com.lounah.vkmc.core.core_vk.domain

import com.lounah.vkmc.core.core_vk.business.commands.market.VKMarketsByCityCommand
import com.lounah.vkmc.core.core_vk.model.Market
import com.vk.api.sdk.VK
import io.reactivex.Single

typealias CityId = String

class GetMarketsByCity : (CityId, Offset) -> Single<List<Market>> {

    override fun invoke(cityId: CityId, offset: Offset): Single<List<Market>> {
        return Single.fromCallable { VK.executeSync(VKMarketsByCityCommand(cityId, offset)) }
    }
}
