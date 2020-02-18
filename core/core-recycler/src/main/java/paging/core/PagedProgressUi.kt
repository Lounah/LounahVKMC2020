package com.lounah.vkmc.core.recycler.paging.core

import com.lounah.vkmc.core.recycler.R
import com.lounah.vkmc.core.recycler.base.ViewTyped

data class PagedProgressUi(
    override val uid: String = "PagedProgressUi",
    override val viewType: Int = R.layout.item_paging_loading
) : ViewTyped
