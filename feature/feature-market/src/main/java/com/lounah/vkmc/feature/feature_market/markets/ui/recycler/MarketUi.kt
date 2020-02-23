package com.lounah.vkmc.feature.feature_market.markets.ui.recycler

import android.graphics.Bitmap
import android.view.View
import com.bumptech.glide.request.RequestOptions
import com.lounah.vkmc.core.recycler.base.ViewTyped
import com.lounah.vkmc.core.recycler.paging.core.BaseViewHolder2
import com.lounah.vkmc.core.ui.imageloader.load
import com.lounah.vkmc.feature.feature_market.R
import kotlinx.android.synthetic.main.item_group.*

data class MarketUi(
    override val uid: String,
    val title: String,
    val type: String,
    val photo: String,
    override val viewType: Int = R.layout.item_group
) : ViewTyped

internal class MarketViewHolder(
    view: View,
    onClick: (MarketUi) -> Unit
) : BaseViewHolder2<MarketUi>(view, onClick) {

    override fun bind(item: MarketUi) {
        super.bind(item)
        icon.load(
            item.photo,
            RequestOptions().circleCrop().encodeFormat(Bitmap.CompressFormat.JPEG).encodeQuality(30)
        )
        name.text = item.title
        type.text = item.type
    }

}
