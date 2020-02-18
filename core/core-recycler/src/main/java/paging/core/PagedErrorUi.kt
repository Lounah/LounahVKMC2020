package com.lounah.vkmc.core.recycler.paging.core

import android.view.View
import com.lounah.vkmc.core.recycler.R
import com.lounah.vkmc.core.recycler.base.BaseViewHolder
import com.lounah.vkmc.core.recycler.base.ViewTyped
import com.lounah.vkmc.core.recycler.base.clicks.RecyclerItemClicksObservable
import kotlinx.android.synthetic.main.item_paging_error.*

data class PagedErrorUi(
    override val uid: String = "PagedErrorUi",
    override val viewType: Int = R.layout.item_paging_error
) : ViewTyped
