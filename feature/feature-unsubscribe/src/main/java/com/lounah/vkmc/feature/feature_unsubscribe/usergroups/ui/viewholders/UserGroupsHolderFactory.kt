package com.lounah.vkmc.feature.feature_unsubscribe.usergroups.ui.viewholders

import android.view.View
import com.lounah.vkmc.core.recycler.base.BaseViewHolder
import com.lounah.vkmc.core.recycler.base.HolderFactory
import com.lounah.vkmc.core.recycler.base.ViewTyped
import com.lounah.vkmc.feature.feature_unsubscribe.R

class UserGroupsHolderFactory : HolderFactory() {

    override fun createViewHolder(view: View, viewType: Int): BaseViewHolder<*>? {
        return when (viewType) {
            R.layout.item_user_groups_header -> BaseViewHolder<ViewTyped>(view)
            R.layout.item_paging_loading -> BaseViewHolder<ViewTyped>(view)
            R.layout.item_paging_error -> PagedErrorViewHolder(view, clicks)
            R.layout.item_user_group -> UserGroupViewHolder(view, clicks)
            else -> null
        }
    }
}