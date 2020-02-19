package com.lounah.vkmc.feature.feature_market.goods.presentation

import com.lounah.vkmc.core.core_vk.domain.City
import com.lounah.vkmc.core.recycler.base.ViewTyped
import com.lounah.vkmc.feature.feature_market.markets.presentation.MarketsAction

sealed class MarketGoodsAction {
    class OnNextPage(val offset: Int) : MarketGoodsAction()
    object OnLoadingError : MarketGoodsAction()
    object OnLoadingStarted : MarketGoodsAction()
    object OnRetryLoadingClicked : MarketGoodsAction()
    class OnGoodsLoaded(val goods: List<ViewTyped>) : MarketGoodsAction()
}
