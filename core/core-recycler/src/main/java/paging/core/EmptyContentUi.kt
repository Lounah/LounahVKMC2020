package com.lounah.vkmc.core.recycler.paging.core

import android.view.View
import androidx.annotation.StringRes
import com.lounah.vkmc.core.recycler.R
import com.lounah.vkmc.core.recycler.base.ViewTyped
import kotlinx.android.synthetic.main.item_empty_content.*

data class EmptyContentUi(@StringRes val textRes: Int) : ViewTyped {
    override val viewType: Int = R.layout.item_empty_content
    override val uid: String = "EmptyContent"
}

class EmptyContentViewHolder(view: View) : BaseViewHolder2<EmptyContentUi>(view) {

    override fun bind(item: EmptyContentUi) {
        emptyText.setText(item.textRes)
    }
}