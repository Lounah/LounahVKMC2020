package com.lounah.vkmc.feature.feature_market.products.presentation

import com.freeletics.rxredux.SideEffect
import com.freeletics.rxredux.reduxStore
import com.jakewharton.rxrelay2.PublishRelay
import com.lounah.vkmc.core.core_vk.domain.MarketId
import com.lounah.vkmc.core.core_vk.domain.Offset
import com.lounah.vkmc.core.core_vk.model.Product
import com.lounah.vkmc.feature.feature_market.products.presentation.MarketGoodsAction.*
import com.lounah.vkmc.feature.feature_market.products.ui.recycler.ProductUi
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.Consumer
import io.reactivex.rxkotlin.ofType
import io.reactivex.schedulers.Schedulers

private typealias MarketGoodsSideEffect = SideEffect<MarketGoodsState, MarketGoodsAction>

class MarketGoodsPresenterFactory(
    private val getGoodsByMarket: (MarketId, Offset) -> Single<List<Product>>,
    private val goodsMapper: (List<Product>) -> List<ProductUi>
) : (MarketId) -> MarketGoodsPresenter {

    override fun invoke(marketId: MarketId): MarketGoodsPresenter {
        return MarketGoodsPresenter(marketId, getGoodsByMarket, goodsMapper)
    }
}

class MarketGoodsPresenter(
    private val marketId: MarketId,
    private val getGoodsByMarket: (MarketId, Offset) -> Single<List<Product>>,
    private val goodsMapper: (List<Product>) -> List<ProductUi>
) {

    private val inputRelay = PublishRelay.create<MarketGoodsAction>()

    val input: Consumer<MarketGoodsAction> = inputRelay
    val state: Observable<MarketGoodsState> = inputRelay
        .reduxStore(
            initialState = MarketGoodsState(marketId),
            sideEffects = listOf(
                loadPagedGoods(),
                initialLoading(),
                repeatLoadGoods()
            ),
            reducer = MarketGoodsState::reduce
        ).distinctUntilChanged()

    private fun initialLoading(): MarketGoodsSideEffect {
        return { _, _ -> loadGoods(0, marketId) }
    }

    private fun loadPagedGoods(): MarketGoodsSideEffect {
        return { actions, _ ->
            actions.ofType<OnNextPage>().flatMap { loadGoods(it.offset, marketId) }
        }
    }

    private fun repeatLoadGoods(): MarketGoodsSideEffect {
        return { actions, state ->
            actions.ofType<OnRetryLoadingClicked>()
                .flatMap { loadGoods(state().pageOffset, marketId) }
        }
    }

    private fun loadGoods(offset: Int, marketId: MarketId): Observable<MarketGoodsAction> {
        return getGoodsByMarket(marketId, offset)
            .subscribeOn(Schedulers.single())
            .observeOn(AndroidSchedulers.mainThread())
            .toObservable()
            .map<MarketGoodsAction> { OnGoodsLoaded(goodsMapper(it)) }
            .onErrorReturnItem(OnLoadingError)
            .startWith(OnLoadingStarted)
    }
}
