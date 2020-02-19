package com.lounah.vkmc.feature.feature_market.goods.presentation

import com.freeletics.rxredux.SideEffect
import com.freeletics.rxredux.reduxStore
import com.jakewharton.rxrelay2.PublishRelay
import com.lounah.vkmc.core.core_vk.domain.City
import com.lounah.vkmc.core.core_vk.domain.GetProductsByMarket
import com.lounah.vkmc.core.core_vk.domain.MarketId
import com.lounah.vkmc.core.core_vk.domain.Offset
import com.lounah.vkmc.core.core_vk.model.Market
import com.lounah.vkmc.core.core_vk.model.Product
import com.lounah.vkmc.feature.feature_market.markets.presentation.MarketsAction
import com.lounah.vkmc.feature.feature_market.markets.presentation.MarketsState
import com.lounah.vkmc.feature.feature_market.markets.presentation.reduce
import com.lounah.vkmc.feature.feature_market.markets.ui.recycler.MarketUi
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.Consumer
import io.reactivex.rxkotlin.ofType
import io.reactivex.schedulers.Schedulers

private typealias MarketGoodsSideEffect = SideEffect<MarketGoodsState, MarketGoodsState>

class MarketsFragmentPresenter(
    private val getGoodsByMarket: (MarketId, Offset) -> Single<List<Product>>,
    private val goodsMapper: (List<Product>) -> List<ProductUi>
) {

    private val inputRelay = PublishRelay.create<MarketsAction>()

    val input: Consumer<MarketsAction> = inputRelay
    val state: Observable<MarketsState> = inputRelay
        .reduxStore(
            initialState = MarketsState(),
            sideEffects = listOf(
                loadPagedMarkets(),
                initialLoading(),
                repeatLoadMarkets(),
                changeCityId(),
                handleCityChanges()
            ),
            reducer = MarketsState::reduce
        ).distinctUntilChanged()

    private fun initialLoading(): MarketsSideEffect {
        return { _, state ->
            loadMarkets(0, state().cityId).mergeWith(
                getCityById(state().cityId).map(MarketsAction::OnCityLoaded)
            )
        }
    }

    private fun loadPagedMarkets(): MarketsSideEffect {
        return { actions, state ->
            actions.ofType<MarketsAction.OnNextPage>().flatMap { loadMarkets(it.offset, state().cityId) }
        }
    }

    private fun handleCityChanges(): MarketsSideEffect {
        return { actions, _ ->
            actions.ofType<MarketsAction.OnCityIdChanged>().flatMap { loadMarkets(0, it.cityId) }
        }
    }

    private fun repeatLoadMarkets(): MarketsSideEffect {
        return { actions, state ->
            actions.ofType<MarketsAction.OnRetryLoadingClicked>()
                .flatMap { loadMarkets(state().pageOffset, state().cityId) }
        }
    }

    private fun changeCityId(): MarketsSideEffect {
        return { actions, _ ->
            actions.ofType<MarketsAction.ChangeCityId>().flatMap {
                getCityById(it.cityId).map<MarketsAction>(MarketsAction::OnCityLoaded)
                    .toObservable()
            }
        }
    }

    private fun loadMarkets(offset: Int, cityId: CityId): Observable<MarketsAction> {
        return getMarketsByCity(cityId, offset)
            .subscribeOn(Schedulers.single())
            .observeOn(AndroidSchedulers.mainThread())
            .toObservable()
            .map<MarketsAction> { MarketsAction.OnMarketsLoaded(userGroupsMapper(it)) }
            .onErrorReturnItem(MarketsAction.OnLoadingError)
            .startWith(MarketsAction.OnLoadingStarted)
    }
}
