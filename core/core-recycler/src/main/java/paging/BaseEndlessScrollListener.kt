package com.lounah.vkmc.core.recycler.paging

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.lounah.vkmc.core.extensions.asType
import io.reactivex.android.MainThreadDisposable

abstract class BaseEndLessScrollListener(
    private val recycler: RecyclerView,
    private val pageSize: Int,
    private val visibleThreshold: Int
) : MainThreadDisposable() {


    val scrollListener: RecyclerView.OnScrollListener
    protected val adapter: RecyclerView.Adapter<*> = recycler.adapter!!
    private val layoutManager: LinearLayoutManager = recycler.layoutManager.asType()
    private val dataObserver: RecyclerView.AdapterDataObserver
    private var previousTotalItemCount = 0
    private var nextPage = false

    init {
        dataObserver = object : RecyclerView.AdapterDataObserver() {
            override fun onChanged() {
                previousTotalItemCount = 0
                nextPage = true
            }

            override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
                previousTotalItemCount += itemCount
                if (pageSize <= previousTotalItemCount && pageSize <= itemCount) {
                    nextPage = true
                }
            }

            override fun onItemRangeChanged(positionStart: Int, itemCount: Int) {
                if (positionStart == previousTotalItemCount && itemCount >= pageSize) {
                    nextPage = true
                }
            }

            override fun onItemRangeRemoved(positionStart: Int, itemCount: Int) {
                if (itemCount >= pageSize) nextPage = true
                previousTotalItemCount -= itemCount
            }
        }
        adapter.registerAdapterDataObserver(dataObserver)
        scrollListener = object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if (!isDisposed && dy != 0) nextPage()
            }
        }
    }

    abstract fun loadMore(itemCount: Int)

    override fun onDispose() {
        adapter.unregisterAdapterDataObserver(dataObserver)
        recycler.removeOnScrollListener(scrollListener)
    }

    private fun nextPage() {
        val lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition()
        val totalItemCount = layoutManager.itemCount
        if (nextPage && lastVisibleItemPosition + visibleThreshold > totalItemCount) {
            previousTotalItemCount = totalItemCount
            nextPage = false
            loadMore(totalItemCount)
        }
    }
}