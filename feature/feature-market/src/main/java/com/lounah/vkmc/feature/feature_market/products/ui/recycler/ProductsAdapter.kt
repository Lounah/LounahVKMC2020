package com.lounah.vkmc.feature.feature_market.products.ui.recycler

import android.view.View
import com.lounah.vkmc.core.extensions.asType
import com.lounah.vkmc.core.recycler.base.ViewTyped
import com.lounah.vkmc.core.recycler.paging.core.BasePagedAdapter
import com.lounah.vkmc.core.recycler.paging.core.BaseViewHolder2
import com.lounah.vkmc.core.recycler.paging.core.EmptyContentViewHolder
import com.lounah.vkmc.feature.feature_market.R

internal class ProductsAdapter(
    private val onProductClicked: (ProductUi) -> Unit,
    onRepeatPagedLoading: () -> Unit
) : BasePagedAdapter(onRepeatPagedLoading) {

    override fun createViewHolder(view: View, viewType: Int): BaseViewHolder2<ViewTyped> {
        return when (viewType) {
            R.layout.item_product -> ProductViewHolder(view, onProductClicked).asType()
            else -> error("Unknown view type")
        }
    }

    override fun onBindViewHolder(holder: BaseViewHolder2<ViewTyped>, position: Int) {
        val item = itemsInternal[position]
        when (item.viewType) {
            R.layout.item_product -> (holder as ProductViewHolder).bind(item.asType())
            R.layout.item_empty_content -> (holder as EmptyContentViewHolder).bind(item.asType())
        }
    }
}
