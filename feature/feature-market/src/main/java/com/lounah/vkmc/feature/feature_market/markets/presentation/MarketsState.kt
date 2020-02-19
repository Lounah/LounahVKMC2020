package com.lounah.vkmc.feature.feature_market.markets.presentation

import com.lounah.vkmc.core.recycler.base.ViewTyped
import com.lounah.vkmc.core.recycler.base.items.ErrorView
import com.lounah.vkmc.core.recycler.base.items.ProgressItem
import com.lounah.vkmc.core.recycler.paging.core.PagedErrorUi
import com.lounah.vkmc.core.recycler.paging.core.PagedProgressUi
import com.lounah.vkmc.feature.feature_market.markets.presentation.MarketsAction.*

private const val MOSCOW_CITY_ID = "1"

data class MarketsState(
    val cityId: String = MOSCOW_CITY_ID,
    val city: String = "",
    val markets: List<ViewTyped> = emptyList(),
    val errorView: ViewTyped = ErrorView(),
    val pagedProgress: ViewTyped = PagedProgressUi(),
    val pagedError: ViewTyped = PagedErrorUi(),
    val pageOffset: Int = 0
)

internal fun MarketsState.reduce(action: MarketsAction): MarketsState {
    return when (action) {
        is OnNextPage -> copy(pageOffset = action.offset)
        is OnMarketsLoaded -> {
            val newItems =
                (markets - ProgressItem - errorView - pagedProgress - pagedError) + action.markets - ProgressItem
            copy(markets = newItems)
        }
        is OnCityLoaded -> copy(city = action.city.inclined)
        is OnCityIdChanged -> copy(cityId = action.cityId, pageOffset = 0, markets = listOf(ProgressItem))
        is OnLoadingStarted -> {
            val newItems = if (pageOffset == 0) listOf(ProgressItem) else {
                (markets - errorView - pagedError - ProgressItem - pagedProgress) + pagedProgress
            }.toList()
            copy(markets = newItems)
        }
        is OnLoadingError -> {
            val newItems = when (pageOffset) {
                0 -> listOf(errorView)
                else -> markets - pagedError + pagedError - pagedProgress
            }
            copy(markets = newItems)
        }
        else -> this
    }
}
