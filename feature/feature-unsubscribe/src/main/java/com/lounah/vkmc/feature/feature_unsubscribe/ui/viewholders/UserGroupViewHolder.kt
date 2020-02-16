package com.lounah.vkmc.feature.feature_unsubscribe.ui.viewholders

import android.view.View
import com.bumptech.glide.request.RequestOptions
import com.lounah.vkmc.core.recycler.base.BaseViewHolder
import com.lounah.vkmc.core.recycler.base.ViewTyped
import com.lounah.vkmc.core.recycler.base.clicks.RecyclerItemClicksObservable
import com.lounah.vkmc.core.ui.imageloader.load
import com.lounah.vkmc.feature.feature_unsubscribe.R
import kotlinx.android.synthetic.main.item_user_group.*

internal class UserGroupUi(
    override val uid: String,
    val photoUrl: String,
    val name: String,
    override val viewType: Int = R.layout.item_user_group
) : ViewTyped

internal class UserGroupViewHolder(
    view: View,
    clicks: RecyclerItemClicksObservable
) : BaseViewHolder<UserGroupUi>(view) {

    init {
        clicks.accept(this)
    }

    override fun bind(item: UserGroupUi) {
        title.text = item.name
        image.load(item.photoUrl, RequestOptions().circleCrop())
    }
}
