package com.lounah.vkmc.feature.feature_unsubscribe.usergroups.ui.recycler

import android.view.View
import androidx.recyclerview.widget.DiffUtil
import com.lounah.vkmc.core.extensions.asType
import com.lounah.vkmc.core.recycler.base.ViewTyped
import com.lounah.vkmc.core.recycler.paging.core.*
import com.lounah.vkmc.feature.feature_unsubscribe.R

internal class UserGroupsAdapter(
    private val onGroupClicked: (UserGroupUi) -> Unit,
    private val onGroupLongClicked: (UserGroupUi) -> Unit,
    onRepeatPagedLoading: () -> Unit
) : BasePagedAdapter(onRepeatPagedLoading) {

    override fun createViewHolder(view: View, viewType: Int): BaseViewHolder2<ViewTyped> {
        return when (viewType) {
            R.layout.item_user_group -> UserGroupViewHolder(view, onGroupClicked, onGroupLongClicked).asType()
            R.layout.item_user_groups_header -> BaseViewHolder2(view)
            else -> error("Unknown view type")
        }
    }

    override fun onBindViewHolder(holder: BaseViewHolder2<ViewTyped>, position: Int) {
        val item = itemsInternal[position]
        when (item.viewType) {
            R.layout.item_user_group -> (holder as UserGroupViewHolder).bind(item.asType())
        }
    }

    override fun setItems(items: List<ViewTyped>) {
        val callback = UserGroupsDiffUtilCallback(itemsInternal, items)
        val diff = DiffUtil.calculateDiff(callback)
        itemsInternal.clear()
        itemsInternal.addAll(items)
        diff.dispatchUpdatesTo(this)
    }
}

internal class UserGroupsDiffUtilCallback(
    private val oldItems: List<ViewTyped>,
    private val newItems: List<ViewTyped>
) : DiffUtil.Callback() {

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldItems[oldItemPosition].uid == newItems[newItemPosition].uid
    }

    override fun getOldListSize(): Int = oldItems.size

    override fun getNewListSize(): Int = newItems.size

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldItems[oldItemPosition] == newItems[newItemPosition]
    }

    override fun getChangePayload(oldItemPosition: Int, newItemPosition: Int): Any? {
        return if (oldItems[oldItemPosition] is UserGroupUi && newItems[newItemPosition] is UserGroupUi) {
            (oldItems[oldItemPosition] as UserGroupUi).isSelected != (newItems[newItemPosition] as UserGroupUi).isSelected
        } else null
    }
}
