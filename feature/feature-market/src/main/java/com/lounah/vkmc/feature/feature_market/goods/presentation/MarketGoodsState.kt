package com.lounah.vkmc.feature.feature_market.goods.presentation

import com.lounah.vkmc.core.core_vk.domain.MarketId
import com.lounah.vkmc.core.recycler.base.ViewTyped
import com.lounah.vkmc.core.recycler.base.items.ErrorView
import com.lounah.vkmc.core.recycler.base.items.ProgressItem
import com.lounah.vkmc.core.recycler.paging.core.PagedErrorUi
import com.lounah.vkmc.core.recycler.paging.core.PagedProgressUi

data class MarketGoodsState(
    val marketId: MarketId,
    val goods: List<ViewTyped> = emptyList(),
    val errorView: ViewTyped = ErrorView(),
    val pagedProgress: ViewTyped = PagedProgressUi(),
    val pagedError: ViewTyped = PagedErrorUi(),
    val pageOffset: Int = 0
)

internal fun MarketGoodsState.reduce(action: MarketGoodsAction): MarketGoodsState {
    return when (action) {
        is MarketGoodsAction.OnNextPage -> copy(pageOffset = action.offset)
        is MarketGoodsAction.OnGoodsLoaded -> {
            val newItems =
                (goods - ProgressItem - errorView - pagedProgress - pagedError) + action.goods - ProgressItem
            copy(goods = newItems)
        }
        is MarketGoodsAction.OnLoadingStarted -> {
            val newItems = if (pageOffset == 0) listOf(ProgressItem) else {
                (goods - errorView - pagedError - ProgressItem - pagedProgress) + pagedProgress
            }.toList()
            copy(goods = newItems)
        }
        is MarketGoodsAction.OnLoadingError -> {
            val newItems = when (pageOffset) {
                0 -> listOf(errorView)
                else -> goods - pagedError + pagedError - pagedProgress
            }
            copy(goods = newItems)
        }
        else -> this
    }
}
