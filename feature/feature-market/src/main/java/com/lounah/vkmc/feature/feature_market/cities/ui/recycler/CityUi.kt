package com.lounah.vkmc.feature.feature_market.cities.ui.recycler

import android.view.View
import com.lounah.vkmc.core.extensions.animateScale
import com.lounah.vkmc.core.recycler.base.ViewTyped
import com.lounah.vkmc.core.recycler.paging.core.BaseViewHolder2
import com.lounah.vkmc.feature.feature_market.R
import kotlinx.android.synthetic.main.item_city.*

data class CityUi(
    override val uid: String,
    val name: String,
    val isChecked: Boolean,
    override val viewType: Int = R.layout.item_city
) : ViewTyped

internal class CityViewHolder(
    view: View, onClicked: (CityUi) -> Unit
) : BaseViewHolder2<CityUi>(view, onClicked) {

    override fun bind(item: CityUi) {
        super.bind(item)
        name.text = item.name
        checkmark.scaleX = if (item.isChecked) 1f else 0f
        checkmark.scaleY = if (item.isChecked) 1f else 0f
    }

    override fun bind(item: CityUi, payloads: List<Any>) {
        super.bind(item, payloads)
        if (item.isChecked) {
            checkmark.animateScale(1, 250)
        } else {
            checkmark.animateScale(0, 250)
        }
    }
}
