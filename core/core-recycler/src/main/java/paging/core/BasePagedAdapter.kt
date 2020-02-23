package com.lounah.vkmc.core.recycler.paging.core

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.lounah.vkmc.core.extensions.asType
import com.lounah.vkmc.core.recycler.R
import com.lounah.vkmc.core.recycler.base.ViewTyped

abstract class BasePagedAdapter(
    private val onRepeatPagedLoading: () -> Unit
) : BaseAdapter() {

    abstract fun createViewHolder(view: View, viewType: Int): BaseViewHolder2<ViewTyped>

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder2<ViewTyped> {
        val view = LayoutInflater.from(parent.context).inflate(viewType, parent, false)
        return when (viewType) {
            R.layout.item_progress -> BaseViewHolder2(view)
            R.layout.item_paging_loading -> BaseViewHolder2(view)
            R.layout.item_empty_content -> EmptyContentViewHolder(view).asType()
            R.layout.item_paging_error -> PagedErrorViewHolder(onRepeatPagedLoading, view).asType()
            R.layout.item_error -> FullScreenErrorViewHolder(onRepeatPagedLoading, view).asType()
            else -> createViewHolder(view, viewType)
        }
    }

    fun isLoading(): Boolean {
        return itemsInternal.any {
            it.viewType == R.layout.item_paging_loading || it.viewType == R.layout.item_progress
        }
    }

    fun isError(): Boolean {
        return itemsInternal.any { it is PagedErrorUi }
    }
}
