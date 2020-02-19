package com.lounah.vkmc.feature.feature_market.markets.presentation

import com.lounah.vkmc.core.recycler.base.ViewTyped

sealed class MarketsAction {
    class OnNextPage(val offset: Int) : MarketsAction()
    object OnLoadingError : MarketsAction()
    object OnLoadingStarted : MarketsAction()
    object OnRetryLoadingClicked : MarketsAction()
    class OnCityIdChanged(val cityId: String) : MarketsAction()
    class OnMarketsLoaded(val markets: List<ViewTyped>) : MarketsAction()
}
