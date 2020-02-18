package com.lounah.vkmc.core.recycler.base

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.lounah.vkmc.core.recycler.R
import com.lounah.vkmc.core.recycler.base.clicks.ItemClick
import com.lounah.vkmc.core.recycler.base.clicks.RecyclerItemClicksObservable
import com.lounah.vkmc.core.recycler.base.items.EmptyContentViewHolder
import com.lounah.vkmc.core.recycler.base.items.ErrorViewHolder
import com.lounah.vkmc.core.recycler.base.items.ProgressItem
import io.reactivex.Observable

abstract class HolderFactory : (ViewGroup, Int) -> BaseViewHolder<ViewTyped> {

    protected val clicks = RecyclerItemClicksObservable()

    abstract fun createViewHolder(view: View, viewType: Int): BaseViewHolder<*>?

    final override fun invoke(viewGroup: ViewGroup, viewType: Int): BaseViewHolder<ViewTyped> {
        val view: View = LayoutInflater.from(viewGroup.context).inflate(viewType, viewGroup, false)
        return when (viewType) {
            R.layout.item_progress -> BaseViewHolder<ProgressItem>(view)
            R.layout.item_error -> ErrorViewHolder(view, clicks)
            R.layout.item_empty_content -> EmptyContentViewHolder(view)
            else -> checkNotNull(createViewHolder(view, viewType)) { "unknown viewType" }
        } as BaseViewHolder<ViewTyped>
    }

    fun clickPosition(vararg viewType: Int): Observable<Int> {
        return clicks.filter { it.viewType in viewType && !it.longClick }.map(ItemClick::position)
    }

    fun longClickPosition(vararg viewType: Int): Observable<Int> {
        return clicks.filter { it.viewType in viewType && it.longClick }.map(ItemClick::position)
    }

    fun clickPosition(viewType: Int, viewId: Int): Observable<Int> {
        return clicks.filter { it.viewType == viewType && it.view.id == viewId }
            .map(ItemClick::position)
    }

    fun repeatOnErrorClicks(): Observable<*> {
        return clicks.filter { it.viewType == R.layout.item_error }
    }
}
