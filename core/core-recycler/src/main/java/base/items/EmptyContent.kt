package com.lounah.vkmc.core.recycler.base.items

import android.view.View
import androidx.annotation.StringRes
import com.lounah.vkmc.core.recycler.R
import com.lounah.vkmc.core.recycler.base.BaseViewHolder
import com.lounah.vkmc.core.recycler.base.ViewTyped
import kotlinx.android.synthetic.main.item_empty_content.*

data class EmptyContent(@StringRes val textRes: Int) : ViewTyped {
    override val viewType: Int = R.layout.item_empty_content
    override val uid: String = "EmptyContent"
}

class EmptyContentViewHolder(view: View) : BaseViewHolder<EmptyContent>(view) {

    override fun bind(item: EmptyContent) {
        emptyText.setText(item.textRes)
    }
}
