package com.lounah.vkmc.core.recycler.paging

import androidx.recyclerview.widget.RecyclerView
import io.reactivex.Observable
import io.reactivex.Observer

const val DEFAULT_PAGE_SIZE = 22
const val DEFAULT_THRESHOLD = 11

fun RecyclerView.endlessScrollObservable(
    pageSize: Int = DEFAULT_PAGE_SIZE,
    threshHold: Int = DEFAULT_THRESHOLD
): Observable<Int> {
    return SimpleEndlessScrollObservable(
        this,
        pageSize,
        threshHold
    )
}

private class SimpleEndlessScrollObservable(
    private val recycler: RecyclerView,
    private val pageSize: Int,
    private val threshHold: Int
) : Observable<Int>() {

    override fun subscribeActual(observer: Observer<in Int>) {
        val listener =
            SimpleEndLessScrollListener(
                recycler,
                observer,
                pageSize,
                threshHold
            )
        observer.onSubscribe(listener)
        recycler.addOnScrollListener(listener.scrollListener)
    }

    class SimpleEndLessScrollListener(
        recycler: RecyclerView,
        private val observer: Observer<in Int>,
        pageSize: Int,
        visibleThreshold: Int
    ) : BaseEndLessScrollListener(recycler, pageSize, visibleThreshold) {

        override fun loadMore(itemCount: Int) = observer.onNext(itemCount)
    }
}
