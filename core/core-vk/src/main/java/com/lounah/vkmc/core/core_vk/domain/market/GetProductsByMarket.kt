package com.lounah.vkmc.core.core_vk.domain.market

import com.lounah.vkmc.core.core_vk.business.commands.market.VKProductsByMarketCommand
import com.lounah.vkmc.core.core_vk.domain.groups.Offset
import com.lounah.vkmc.core.core_vk.model.Product
import com.vk.api.sdk.VK
import io.reactivex.Single

typealias MarketId = String

class GetProductsByMarket : (MarketId, Offset) -> Single<List<Product>> {

    override fun invoke(marketId: MarketId, offset: Offset): Single<List<Product>> {
        return Single.fromCallable { VK.executeSync(VKProductsByMarketCommand(marketId, offset)) }
    }
}
