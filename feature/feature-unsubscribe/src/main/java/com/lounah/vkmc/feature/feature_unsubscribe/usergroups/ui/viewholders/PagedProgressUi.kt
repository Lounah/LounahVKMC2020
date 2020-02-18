package com.lounah.vkmc.feature.feature_unsubscribe.usergroups.ui.viewholders

import com.lounah.vkmc.core.recycler.base.ViewTyped
import com.lounah.vkmc.feature.feature_unsubscribe.R

data class PagedProgressUi(
    override val uid: String = "PagedProgressUi",
    override val viewType: Int = R.layout.item_paging_loading
) : ViewTyped