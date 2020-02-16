package com.lounah.vkmc.common

import com.lounah.vkmc.core.recycler.base.ViewTyped
import com.lounah.vkmc.core.recycler.base.items.ErrorView
import io.reactivex.Completable
import io.reactivex.Observable

typealias Offset = Int

class GetPagedItems(private val fetchItems: (Offset) -> Completable,
                    private val getAll: () -> Observable<List<ViewTyped>>,
                    private val connectionStateEvent: Observable<Boolean>,
                    private val loadMoreItem: ViewTyped,
                    private val errorLoadingMoreItem: ViewTyped,
                    fullscreenProgress: ViewTyped,
                    fullscreenErrorView: ErrorView,
                    private val getAllAndObserve: () -> Observable<List<ViewTyped>> = getAll) :
        (Offset) -> Observable<List<ViewTyped>> {

    private val progress = listOf(fullscreenProgress)
    private val fullscreenError = listOf(fullscreenErrorView)

    override fun invoke(offset: Offset): Observable<List<ViewTyped>> =
        connectionStateEvent.switchMap {
            loadItems(offset).onErrorResumeNext(onError(offset))
        }

    private fun loadItems(offset: Offset): Observable<List<ViewTyped>> {
        return Observable.merge(local(offset), remote(offset))
    }

    private fun local(offset: Offset) = getAllAndObserve().map {
        when {
            it.isEmpty() -> progress
            offset == 0 -> listOf(loadMoreItem) + it
            else -> it + loadMoreItem
        }
    }

    private fun remote(offset: Offset): Observable<List<ViewTyped>> =
        fetchItems(offset).andThen(getAllAndObserve())

    private fun onError(offset: Offset): Observable<List<ViewTyped>> = getAll().map {
        when {
            it.isEmpty() -> fullscreenError
            offset == 0 -> listOf(errorLoadingMoreItem) + it
            else -> it + errorLoadingMoreItem
        }
    }
}
