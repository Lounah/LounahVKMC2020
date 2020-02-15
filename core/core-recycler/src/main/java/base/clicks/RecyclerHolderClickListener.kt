package com.lounah.vkmc.core.recycler.base.clicks

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.lounah.vkmc.core.recycler.base.BaseViewHolder
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.subjects.PublishSubject

interface RecyclerHolderClickListener {
    fun accept(viewHolder: BaseViewHolder<*>, onClick: () -> Unit = {})
    fun accept(view: View, viewHolder: BaseViewHolder<*>, onClick: () -> Unit = {})
}

data class ItemClick(val viewType: Int, val position: Int, val view: View)
class RecyclerItemClicksObservable : Observable<ItemClick>(),
    RecyclerHolderClickListener {

    private val source: PublishSubject<ItemClick> = PublishSubject.create()

    override fun accept(viewHolder: BaseViewHolder<*>, onClick: () -> Unit) {
        viewHolder.itemView.run { setOnClickListener(Listener(source, viewHolder, this, onClick)) }
    }

    override fun accept(view: View, viewHolder: BaseViewHolder<*>, onClick: () -> Unit) {
        view.setOnClickListener(Listener(source, viewHolder, view, onClick))
    }

    override fun subscribeActual(observer: Observer<in ItemClick>) {
        source.subscribe(observer)
    }

    class Listener(
        private val source: Observer<ItemClick>,
        private val viewHolder: BaseViewHolder<*>,
        private val clickedView: View,
        private val onClick: () -> Unit
    ) : View.OnClickListener {

        override fun onClick(v: View) {
            if (viewHolder.adapterPosition != RecyclerView.NO_POSITION) {
                onClick()
                source.onNext(
                    ItemClick(
                        viewHolder.itemViewType,
                        viewHolder.adapterPosition,
                        clickedView
                    )
                )
            }
        }
    }
}
