package com.lounah.vkmc.core.recycler.base

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.extensions.LayoutContainer

open class BaseViewHolder<T : ViewTyped>(override val containerView: View) :
    RecyclerView.ViewHolder(containerView), LayoutContainer {

    open fun bind(item: T) = Unit

    open fun bind(item: T, payload: List<Any>) = Unit
}
