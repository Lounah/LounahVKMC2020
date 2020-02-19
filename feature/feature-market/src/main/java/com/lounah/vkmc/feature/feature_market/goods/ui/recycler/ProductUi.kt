package com.lounah.vkmc.feature.feature_market.goods.ui.recycler

import android.view.View
import com.lounah.vkmc.core.recycler.base.ViewTyped
import com.lounah.vkmc.core.recycler.paging.core.BaseViewHolder2

data class ProductUi(
    override val uid: String,
    val name: String,
    val price: String,
    override val viewType: Int = 0
) : ViewTyped

class ProductViewHolder(
    view: View,
    onClicked: (ProductUi) -> Unit
) : BaseViewHolder2<ProductUi>(view, onClicked) {

    override fun bind(item: ProductUi) {
        super.bind(item)

    }
}
