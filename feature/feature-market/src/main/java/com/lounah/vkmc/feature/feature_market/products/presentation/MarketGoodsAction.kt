package com.lounah.vkmc.feature.feature_market.products.presentation

import com.lounah.vkmc.core.recycler.base.ViewTyped

sealed class MarketGoodsAction {
    class OnNextPage(val offset: Int) : MarketGoodsAction()
    object OnLoadingError : MarketGoodsAction()
    object OnLoadingStarted : MarketGoodsAction()
    object OnRetryLoadingClicked : MarketGoodsAction()
    class OnGoodsLoaded(val goods: List<ViewTyped>) : MarketGoodsAction()
}
