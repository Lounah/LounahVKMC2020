package com.lounah.vkmc.feature.feature_unsubscribe.usergroups.ui.recycler

import androidx.recyclerview.widget.GridLayoutManager
import com.lounah.vkmc.core.recycler.base.ViewTyped
import com.lounah.vkmc.feature.feature_unsubscribe.R

class UserGroupsSpanSizeLookup
    (private val items: () -> List<ViewTyped>) :
    GridLayoutManager.SpanSizeLookup() {
    override fun isSpanIndexCacheEnabled(): Boolean {
        return false
    }
    override fun getSpanSize(position: Int): Int {
        val isProgress = items()[position].viewType == R.layout.item_paging_loading
        val isErrorLoading = items()[position].viewType == R.layout.item_paging_error

        return if (position == 0 || isProgress || isErrorLoading) 3 else 1
    }
}