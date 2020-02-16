package com.lounah.vkmc.common

import com.lounah.vkmc.core.recycler.base.ReduxAction
import com.lounah.vkmc.core.recycler.base.ViewTyped
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject

/*
class LoadPagedItemsActionCreator(private val getItems: (Offset) -> Observable<List<ViewTyped>>,
                                  showItems: ActionMapper<DiffResult<ViewTyped>>,
                                  private val emptyView: ViewTyped,
                                  private val diffCalculator: DiffCalculator<ViewTyped>) :
        ActionCreator<Observable<Offset>> {

    private val itemsReceiver = BehaviorSubject.create<List<ViewTyped>>()
    private val itemsObserver = itemsReceiver.scan(DiffResult.EmptyDiffResult) { oldDiff: DiffResult<ViewTyped>, items ->
        val newItems = if (items.isEmpty()) listOf(emptyView) else items
        diffCalculator(oldDiff.items, newItems)
    }
            .map(showItems)

    override fun invoke(loadEvent: Observable<Offset>): Observable<Action> {
        val loadNewItems = loadEvent.switchMap { offset ->
            getItems(offset).doOnNext(itemsReceiver::onNext)
                    .map { EmptyAction }
        }
        return Observable.merge(loadNewItems, itemsObserver)
                .subscribeOn(Schedulers.io())
    }
}
 */

class LoadPagedItems(private val getItems: (Offset) -> Observable<List<ViewTyped>>,
                     private val emptyView: ViewTyped) : (Offset) -> Observable<ReduxAction> {

    private val itemsReceiver = BehaviorSubject.create<List<ViewTyped>>()
    private val itemsObserver = itemsReceiver.scan(emptyList()) { _: List<ViewTyped>, items ->
        val newItems = if (items.isEmpty()) listOf(emptyView) else items
        newItems
    }.flatMap { Observable.empty<ReduxAction>() }

    override fun invoke(offset: Offset): Observable<ReduxAction> {
        val loadNewItems = getItems(offset).doOnNext(itemsReceiver::onNext)
            .flatMap { Observable.empty<ReduxAction>() }

        return Observable.merge(loadNewItems, itemsObserver)
            .subscribeOn(Schedulers.io())
    }
}
