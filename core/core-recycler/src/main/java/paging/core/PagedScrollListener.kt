package com.lounah.vkmc.core.recycler.paging.core

import androidx.recyclerview.widget.RecyclerView
import com.lounah.vkmc.core.extensions.asType

fun RecyclerView.pagedScrollListener(onNextPage: (Int) -> Unit) {
    val adapter = adapter.asType<BasePagedAdapter>()
    addOnScrollListener(object : RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
            if (!recyclerView.canScrollVertically(1) && !adapter.isLoading() && !adapter.isError()) {
                onNextPage(adapter.itemCount)
            }
        }
    })
}
