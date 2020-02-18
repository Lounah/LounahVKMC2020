package com.lounah.vkmc.feature.feature_unsubscribe.usergroups.ui.viewholders

import android.view.View
import com.lounah.vkmc.core.recycler.base.BaseViewHolder
import com.lounah.vkmc.core.recycler.base.ViewTyped
import com.lounah.vkmc.core.recycler.base.clicks.RecyclerItemClicksObservable
import com.lounah.vkmc.feature.feature_unsubscribe.R
import kotlinx.android.synthetic.main.item_paging_error.*

internal data class PagedErrorUi(
    override val uid: String = "PagedErrorUi",
    override val viewType: Int = R.layout.item_paging_error
) : ViewTyped

internal class PagedErrorViewHolder(
    view: View,
    clicks: RecyclerItemClicksObservable
) :
    BaseViewHolder<PagedErrorUi>(view) {

    init {
        clicks.accept(btnRepeat, this@PagedErrorViewHolder)
    }
}