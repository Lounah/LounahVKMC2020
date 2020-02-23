package com.lounah.vkmc.feature.feature_market.products.presentation

import com.lounah.vkmc.core.core_vk.domain.MarketId
import com.lounah.vkmc.core.recycler.base.ViewTyped
import com.lounah.vkmc.core.recycler.base.items.ErrorView
import com.lounah.vkmc.core.recycler.base.items.ProgressItem
import com.lounah.vkmc.core.recycler.paging.core.EmptyContentUi
import com.lounah.vkmc.core.recycler.paging.core.PagedErrorUi
import com.lounah.vkmc.core.recycler.paging.core.PagedProgressUi
import com.lounah.vkmc.feature.feature_market.R
import com.lounah.vkmc.feature.feature_market.products.presentation.MarketGoodsAction.*

data class MarketGoodsState(
    val marketId: MarketId,
    val goods: List<ViewTyped> = emptyList(),
    val errorView: ViewTyped = ErrorView(),
    val emptyView: ViewTyped = EmptyContentUi(R.string.market_no_goods),
    val pagedProgress: ViewTyped = PagedProgressUi(),
    val pagedError: ViewTyped = PagedErrorUi(),
    val pageOffset: Int = 0
)

internal fun MarketGoodsState.reduce(action: MarketGoodsAction): MarketGoodsState {
    return when (action) {
        is OnNextPage -> copy(pageOffset = action.offset)
        is OnGoodsLoaded -> {
            val newItems =
                (goods - ProgressItem - errorView - pagedProgress - pagedError - emptyView) + action.goods - ProgressItem
            if (newItems.isEmpty())
                copy(goods = listOf(emptyView))
            else
                copy(goods = newItems)
        }
        is OnLoadingStarted -> {
            val newItems = if (pageOffset == 0) listOf(ProgressItem) else {
                (goods - errorView - pagedError - ProgressItem - pagedProgress - emptyView) + pagedProgress
            }.toList()
            copy(goods = newItems)
        }
        is OnLoadingError -> {
            val newItems = when (pageOffset) {
                0 -> listOf(errorView)
                else -> goods - pagedError + pagedError - pagedProgress - emptyView
            }
            copy(goods = newItems)
        }
        else -> this
    }
}
