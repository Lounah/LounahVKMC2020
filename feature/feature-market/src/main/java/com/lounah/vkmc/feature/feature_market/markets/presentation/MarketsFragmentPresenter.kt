package com.lounah.vkmc.feature.feature_market.markets.presentation

import android.util.Log
import com.freeletics.rxredux.SideEffect
import com.freeletics.rxredux.reduxStore
import com.jakewharton.rxrelay2.PublishRelay
import com.lounah.vkmc.core.core_vk.domain.CityId
import com.lounah.vkmc.core.core_vk.domain.Offset
import com.lounah.vkmc.core.core_vk.model.Market
import com.lounah.vkmc.feature.feature_market.markets.presentation.MarketsAction.*
import com.lounah.vkmc.feature.feature_market.markets.ui.recycler.MarketUi
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.Consumer
import io.reactivex.rxkotlin.ofType
import io.reactivex.schedulers.Schedulers

private typealias MarketsSideEffect = SideEffect<MarketsState, MarketsAction>

class MarketsFragmentPresenter(
    private val getMarketsByCity: (CityId, Offset) -> Single<List<Market>>,
    private val userGroupsMapper: (List<Market>) -> List<MarketUi>
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
                handleCityChanges()
            ),
            reducer = MarketsState::reduce
        ).distinctUntilChanged()

    private fun initialLoading(): MarketsSideEffect {
        return { _, state -> loadMarkets(0, state().cityId) }
    }

    private fun loadPagedMarkets(): MarketsSideEffect {
        return { actions, state ->
            actions.ofType<OnNextPage>().flatMap { loadMarkets(it.offset, state().cityId) }
        }
    }

    private fun handleCityChanges(): MarketsSideEffect {
        return { actions, _ ->
            actions.ofType<OnCityIdChanged>().flatMap { loadMarkets(0, it.cityId) }
        }
    }

    private fun repeatLoadMarkets(): MarketsSideEffect {
        return { actions, state ->
            actions.ofType<OnRetryLoadingClicked>()
                .flatMap { loadMarkets(state().pageOffset, state().cityId) }
        }
    }

    private fun loadMarkets(offset: Int, cityId: CityId): Observable<MarketsAction> {
        return getMarketsByCity(cityId, offset)
            .subscribeOn(Schedulers.single())
            .observeOn(AndroidSchedulers.mainThread())
            .toObservable()
            .map<MarketsAction> { OnMarketsLoaded(userGroupsMapper(it)) }
            .doOnError { Log.i("error", "$it") }
            .onErrorReturnItem(OnLoadingError)
            .startWith(OnLoadingStarted)
    }
}
