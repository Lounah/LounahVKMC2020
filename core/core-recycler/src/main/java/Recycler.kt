package com.lounah.vkmc.core.recycler

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.lounah.vkmc.core.recycler.base.HolderFactory
import com.lounah.vkmc.core.recycler.base.ViewTyped
import io.reactivex.Observable

interface RecyclerBuilder {
    var layoutManager: RecyclerView.LayoutManager
    var itemDecoration: List<RecyclerView.ItemDecoration>
}

@Suppress("VarCouldBeVal")
interface Recycler<T : ViewTyped> {
    fun setItems(items: List<T>)
    fun clickedItem(vararg viewType: Int): Observable<T>
    fun clickedViewId(viewType: Int, viewId: Int): Observable<T>
    fun repeatOnErrorClick(): Observable<*>

    val adapter: Adapter<T>

    companion object {
        operator fun <T : ViewTyped> invoke(
            recyclerView: RecyclerView,
            holderFactory: HolderFactory,
            init: RecyclerBuilder.() -> Unit = {}
        ): Recycler<T> {
            val layoutManager = recyclerView.layoutManager
                ?: LinearLayoutManager(recyclerView.context)
            val builder: RecyclerBuilder = object : RecyclerBuilder {
                override var layoutManager: RecyclerView.LayoutManager = layoutManager
                override var itemDecoration: List<RecyclerView.ItemDecoration> = emptyList()
            }.apply(init)
            return RecyclerImpl(builder, holderFactory, recyclerView)
        }
    }
}

private class RecyclerImpl<T : ViewTyped>(
    builder: RecyclerBuilder,
    private val holderFactory: HolderFactory,
    recyclerView: RecyclerView
) : Recycler<T> {

    private val recyclerAdapter = Adapter<T>(holderFactory)

    init {
        recyclerView.adapter = recyclerAdapter
        recyclerView.layoutManager = builder.layoutManager
        recyclerView.setHasFixedSize(true)
        builder.itemDecoration.forEach(recyclerView::addItemDecoration)
    }

    override fun setItems(items: List<T>) {
        recyclerAdapter.items = items
    }

    override fun clickedItem(vararg viewType: Int): Observable<T> {
        return holderFactory.clickPosition(*viewType).map(recyclerAdapter.items::get)
    }

    override fun clickedViewId(viewType: Int, viewId: Int): Observable<T> {
        return holderFactory.clickPosition(viewType, viewId).map(recyclerAdapter.items::get)
    }

    override fun repeatOnErrorClick(): Observable<*> = holderFactory.repeatOnErrorClicks()

    override val adapter: Adapter<T>
        get() = recyclerAdapter
}
