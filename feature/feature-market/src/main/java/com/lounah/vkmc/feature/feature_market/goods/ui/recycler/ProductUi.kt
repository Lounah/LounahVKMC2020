package com.lounah.vkmc.feature.feature_market.goods.ui.recycler

import android.os.Parcelable
import android.view.View
import com.bumptech.glide.request.RequestOptions
import com.lounah.vkmc.core.recycler.base.ViewTyped
import com.lounah.vkmc.core.recycler.paging.core.BaseViewHolder2
import com.lounah.vkmc.core.ui.imageloader.load
import com.lounah.vkmc.feature.feature_market.R
import kotlinx.android.parcel.Parcelize
import kotlinx.android.synthetic.main.item_product.*

@Parcelize
data class ProductUi(
    override val uid: String,
    val name: String,
    val photo: String,
    val price: String,
    val description: String,
    override val viewType: Int = R.layout.item_product
) : ViewTyped, Parcelable

class ProductViewHolder(
    view: View,
    onClicked: (ProductUi) -> Unit
) : BaseViewHolder2<ProductUi>(view, onClicked) {

    override fun bind(item: ProductUi) {
        super.bind(item)
        image.load(item.photo, RequestOptions().encodeQuality(80))
        name.text = item.name
        price.text = item.price
    }
}
