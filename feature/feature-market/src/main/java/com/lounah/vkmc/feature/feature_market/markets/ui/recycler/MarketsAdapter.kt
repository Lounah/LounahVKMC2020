package com.lounah.vkmc.feature.feature_market.markets.ui.recycler

import android.view.View
import com.lounah.vkmc.core.extensions.asType
import com.lounah.vkmc.core.recycler.base.ViewTyped
import com.lounah.vkmc.core.recycler.paging.core.BasePagedAdapter
import com.lounah.vkmc.core.recycler.paging.core.BaseViewHolder2
import com.lounah.vkmc.feature.feature_market.R

internal class MarketsAdapter(
    private val onMarketClicked: (MarketUi) -> Unit,
    onRepeatPagedLoading: () -> Unit
) : BasePagedAdapter(onRepeatPagedLoading) {

    override fun createViewHolder(view: View, viewType: Int): BaseViewHolder2<ViewTyped> {
        return when (viewType) {
            R.layout.item_group -> MarketViewHolder(view, onMarketClicked).asType()
            else -> error("Unknown view type")
        }
    }

    override fun onBindViewHolder(holder: BaseViewHolder2<ViewTyped>, position: Int) {
        val item = itemsInternal[position]
        when (item.viewType) {
            R.layout.item_group -> (holder as MarketViewHolder).bind(item.asType())
        }
    }
}
