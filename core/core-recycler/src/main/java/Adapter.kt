package com.lounah.vkmc.core.recycler

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.lounah.vkmc.core.recycler.base.BaseViewHolder
import com.lounah.vkmc.core.recycler.base.HolderFactory
import com.lounah.vkmc.core.recycler.base.ViewTyped

class Adapter<T : ViewTyped>(private val holderFactory: HolderFactory) :
    RecyclerView.Adapter<BaseViewHolder<ViewTyped>>() {

    private val localItems: MutableList<T> = mutableListOf()

    var items: List<T>
        get() = localItems
        set(newItems) {
            localItems.clear()
            localItems.addAll(newItems)
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<ViewTyped> =
        holderFactory(parent, viewType)

    override fun getItemCount(): Int = localItems.size

    override fun onBindViewHolder(holder: BaseViewHolder<ViewTyped>, position: Int) =
        holder.bind(localItems[position])

    override fun onBindViewHolder(
        holder: BaseViewHolder<ViewTyped>,
        position: Int,
        payloads: MutableList<Any>
    ) {
        if (payloads.isNotEmpty()) holder.bind(localItems[position], payloads)
        else super.onBindViewHolder(holder, position, payloads)
    }

    fun updateItems(newItems: List<T>) {
        localItems.clear()
        localItems.addAll(newItems)
    }

    fun getItem(position: Int): T = localItems[position]

    fun isEmpty(): Boolean = localItems.isEmpty()

    override fun getItemViewType(position: Int): Int {
        return localItems[position].viewType
    }
}
