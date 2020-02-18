package com.lounah.vkmc.feature.feature_unsubscribe.usergroups.ui.recycler

import android.view.View
import com.bumptech.glide.request.RequestOptions
import com.lounah.vkmc.core.extensions.animateScale
import com.lounah.vkmc.core.extensions.asType
import com.lounah.vkmc.core.recycler.base.ViewTyped
import com.lounah.vkmc.core.recycler.paging.core.BaseViewHolder2
import com.lounah.vkmc.core.ui.imageloader.load
import com.lounah.vkmc.feature.feature_unsubscribe.R
import kotlinx.android.synthetic.main.item_user_group.*

data class UserGroupUi(
    override val uid: String,
    val photoUrl: String,
    val name: String,
    val isSelected: Boolean = false,
    override val viewType: Int = R.layout.item_user_group
) : ViewTyped

class UserGroupViewHolder(
    containerView: View,
    onClick: (UserGroupUi) -> Unit,
    onLongClick: (UserGroupUi) -> Unit
) : BaseViewHolder2<UserGroupUi>(containerView, onClick, onLongClick) {

    override fun bind(item: UserGroupUi) {
        super.bind(item)
        title.text = item.name
        image.load(
            item.photoUrl,
            RequestOptions().circleCrop()
        )
        imageContainer.isSelected = item.isSelected
        checkmark.scaleX = if (item.isSelected) 1f else 0f
        checkmark.scaleY = if (item.isSelected) 1f else 0f
    }

    override fun bind(item: UserGroupUi, payloads: List<Any>) {
        imageContainer.isSelected = item.isSelected
        if (imageContainer.isSelected) {
            checkmark.animateScale(1, 250)
        } else {
            checkmark.animateScale(0, 250)
        }
    }
}
