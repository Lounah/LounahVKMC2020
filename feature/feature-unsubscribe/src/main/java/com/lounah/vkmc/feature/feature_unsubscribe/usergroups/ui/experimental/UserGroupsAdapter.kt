package com.lounah.vkmc.feature.feature_unsubscribe.usergroups.ui.experimental

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.lounah.vkmc.core.extensions.asType
import com.lounah.vkmc.core.recycler.base.ViewTyped
import com.lounah.vkmc.feature.feature_unsubscribe.R
import com.lounah.vkmc.feature.feature_unsubscribe.usergroups.ui.viewholders.UserGroupUi

internal class UserGroupsAdapter(
    private val onGroupClicked: (UserGroupUi) -> Unit,
    private val onGroupLongClicked: (UserGroupUi) -> Unit,
    private val onRepeatPagedLoading: () -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val itemsInternal = mutableListOf<ViewTyped>()

    val items: List<ViewTyped>
        get() = itemsInternal

    override fun getItemViewType(position: Int): Int = itemsInternal[position].viewType

    override fun getItemCount(): Int = itemsInternal.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(viewType, parent, false)
        return when (viewType) {
            R.layout.item_user_group -> UserGroupViewHolder(
                view
            ).apply {
                itemView.setOnClickListener { onGroupClicked(itemView.tag.asType()) }
                itemView.setOnLongClickListener {
                    onGroupLongClicked(itemView.tag.asType())
                    true
                }
            }
            R.layout.item_progress -> FullScreenProgressViewHolder(view)
            R.layout.item_paging_loading -> PagedProgressViewHolder(view)
            R.layout.item_paging_error -> PagedErrorViewHolder(onRepeatPagedLoading, view)
            R.layout.item_user_groups_header -> GroupHeaderViewHolder(view)
            R.layout.item_error -> FullScreenErrorViewHolder(onRepeatPagedLoading, view)
            else -> error("Unknown view type")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = itemsInternal[position]
        when (item.viewType) {
            R.layout.item_user_group -> (holder as UserGroupViewHolder).bind(item.asType())
        }
    }

    fun setItems(items: List<ViewTyped>) {
        val callback =
            UserGroupsDiffCallback(
                itemsInternal,
                items
            )
        val diff = DiffUtil.calculateDiff(callback)
        itemsInternal.clear()
        itemsInternal.addAll(items)
        diff.dispatchUpdatesTo(this)
    }
}

class UserGroupsDiffCallback(
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
}