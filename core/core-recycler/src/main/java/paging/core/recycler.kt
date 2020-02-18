package com.lounah.vkmc.core.recycler.paging.core

import android.view.View
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.lounah.vkmc.core.extensions.asType
import com.lounah.vkmc.core.recycler.base.BaseViewHolder
import com.lounah.vkmc.core.recycler.base.ViewTyped
import com.lounah.vkmc.core.ui.util.ClickLock
import kotlinx.android.extensions.LayoutContainer

open class BaseViewHolder2<T : ViewTyped>(
    override val containerView: View,
    onClick: ((T) -> Unit)? = null,
    onLongClick: ((T) -> Unit)? = null
) : RecyclerView.ViewHolder(containerView), LayoutContainer {

    init {
        onClick?.let { itemView.setOnClickListener { it(itemView.tag.asType()) } }
        onLongClick?.let {
            itemView.setOnLongClickListener {
                it(itemView.tag.asType())
                true
            }
        }
    }

    open fun bind(item: T) {
        itemView.tag = item
    }

    open fun bind(item: T, payloads: List<Any>) = Unit
}

abstract class BaseAdapter : RecyclerView.Adapter<BaseViewHolder2<ViewTyped>>() {

    protected val itemsInternal = mutableListOf<ViewTyped>()

    val items: List<ViewTyped>
        get() = itemsInternal

    open fun setItems(items: List<ViewTyped>) {
        val callback = ViewTypedDiffCallback(itemsInternal, items)
        val diff = DiffUtil.calculateDiff(callback)
        itemsInternal.clear()
        itemsInternal.addAll(items)
        diff.dispatchUpdatesTo(this)
    }

    override fun getItemViewType(position: Int): Int = itemsInternal[position].viewType

    override fun getItemCount(): Int = itemsInternal.size

    override fun onBindViewHolder(
        holder: BaseViewHolder2<ViewTyped>,
        position: Int,
        payloads: MutableList<Any>
    ) {
        if (payloads.isNotEmpty()) holder.bind(itemsInternal[position], payloads)
        else super.onBindViewHolder(holder, position, payloads)
    }
}

class ViewTypedDiffCallback(
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
