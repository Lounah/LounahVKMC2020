package com.lounah.vkmc.feature.feature_market.cities.ui.recycler

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import com.lounah.vkmc.core.extensions.asType
import com.lounah.vkmc.core.recycler.base.ViewTyped
import com.lounah.vkmc.core.recycler.paging.core.BaseAdapter
import com.lounah.vkmc.core.recycler.paging.core.BaseViewHolder2
import com.lounah.vkmc.feature.feature_market.R

internal class CitiesAdapter(
    private val onCityClicked: (CityUi) -> Unit
) : BaseAdapter() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder2<ViewTyped> {
        val view = LayoutInflater.from(parent.context).inflate(viewType, parent, false)
        return when (viewType) {
            R.layout.item_city -> CityViewHolder(view, onCityClicked).asType()
            else -> error("Unknown view type")
        }
    }

    override fun onBindViewHolder(holder: BaseViewHolder2<ViewTyped>, position: Int) {
        val item = itemsInternal[position]
        when (item.viewType) {
            R.layout.item_city -> (holder as CityViewHolder).bind(item.asType())
        }
    }

    override fun setItems(items: List<ViewTyped>) {
        val callback = CitiesDiffUtilCallback(itemsInternal, items)
        val diff = DiffUtil.calculateDiff(callback)
        itemsInternal.clear()
        itemsInternal.addAll(items)
        diff.dispatchUpdatesTo(this)
    }
}

internal class CitiesDiffUtilCallback(
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
        return if (oldItems[oldItemPosition] is CityUi && newItems[newItemPosition] is CityUi) {
            (oldItems[oldItemPosition] as CityUi).isChecked != (newItems[newItemPosition] as CityUi).isChecked
        } else null
    }
}
