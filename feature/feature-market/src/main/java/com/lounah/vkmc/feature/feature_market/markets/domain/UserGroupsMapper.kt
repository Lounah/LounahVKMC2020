package com.lounah.vkmc.feature.feature_market.markets.domain

import com.lounah.vkmc.core.core_vk.model.Market
import com.lounah.vkmc.feature.feature_market.markets.ui.recycler.MarketUi

class UserGroupsMapper : (List<Market>) -> List<MarketUi> {

    override fun invoke(markets: List<Market>): List<MarketUi> {
        return markets.map {
            MarketUi(
                uid = it.id,
                title = it.name,
                type = "Открытая группа",
                photo = it.photo
            )
        }
    }
}
