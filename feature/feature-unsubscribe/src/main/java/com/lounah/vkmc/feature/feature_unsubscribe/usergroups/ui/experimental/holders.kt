package com.lounah.vkmc.feature.feature_unsubscribe.usergroups.ui.experimental

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.request.RequestOptions
import com.lounah.vkmc.core.ui.imageloader.load
import com.lounah.vkmc.feature.feature_unsubscribe.usergroups.ui.viewholders.UserGroupUi
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_paging_error.view.*
import kotlinx.android.synthetic.main.item_user_group.*
import kotlinx.android.synthetic.main.item_paging_error.btnRepeat as PagingRepeat

class UserGroupViewHolder(override val containerView: View) :
    RecyclerView.ViewHolder(containerView), LayoutContainer {

    fun bind(item: UserGroupUi) {
        title.text = item.name
        image.load(
            item.photoUrl,
            RequestOptions().circleCrop()
        )
        containerView.tag = item
    }
}

class FullScreenProgressViewHolder(override val containerView: View) :
    RecyclerView.ViewHolder(containerView), LayoutContainer

class FullScreenErrorViewHolder(onRepeat: () -> Unit, override val containerView: View) :
    RecyclerView.ViewHolder(containerView), LayoutContainer {

    init {
//        btnRepeat.setOnClickListener { onRepeat() }
    }
}

class PagedProgressViewHolder(override val containerView: View) :
    RecyclerView.ViewHolder(containerView), LayoutContainer

class GroupHeaderViewHolder(override val containerView: View) :
    RecyclerView.ViewHolder(containerView), LayoutContainer

class PagedErrorViewHolder(onRepeat: () -> Unit, override val containerView: View) :
    RecyclerView.ViewHolder(containerView), LayoutContainer {
    init {
        PagingRepeat.btnRepeat.setOnClickListener { onRepeat() }
    }
}