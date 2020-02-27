package com.lounah.vkmc.feature.feature_map.map.presentation

import android.util.Log
import com.freeletics.rxredux.SideEffect
import com.freeletics.rxredux.reduxStore
import com.google.android.gms.maps.model.LatLng
import com.jakewharton.rxrelay2.PublishRelay
import com.lounah.vkmc.core.core_vk.domain.market.CityId
import com.lounah.vkmc.feature.feature_map.map.presentation.EventsMapAction.*
import com.lounah.vkmc.feature.feature_map.map.ui.map.markers.MapMarker
import io.reactivex.Observable
import io.reactivex.functions.Consumer
import io.reactivex.rxkotlin.ofType
import io.reactivex.schedulers.Schedulers.io

private typealias EventsMapSideEffect = SideEffect<EventsMapState, EventsMapAction>

class EventsMapPresenter(
    private val getNearestCity: (LatLng) -> Observable<CityId>,
    private val getMapItems: (CityId, RenderType) -> Observable<List<MapMarker>>
) {

    private val inputRelay = PublishRelay.create<EventsMapAction>()

    val input: Consumer<EventsMapAction> = inputRelay
    val state: Observable<EventsMapState> = inputRelay
        .reduxStore(
            initialState = EventsMapState(),
            sideEffects = listOf(
                handleMapIdle(),
                handleRenderTypeChanges(),
                handleRetryLoading()
            ),
            reducer = EventsMapState::reduce
        )

    private fun handleMapIdle(): EventsMapSideEffect {
        return { actions, state ->
            actions.ofType<OnMapIdle>().switchMap {
                getNearestCity(it.cameraLatLng)
                    .subscribeOn(io())
                    .filter { cityId -> state().cityId != cityId }
                    .switchMap { cityId ->
                        Observable.merge(
                            getMapItems(cityId, state().renderType)
                                .subscribeOn(io())
                                .map<EventsMapAction>(::OnMapItemsLoaded)
                                .startWith(StartLoading)
                                .doOnError { Log.i("error", "$it") }
                                .onErrorReturnItem(ErrorLoading),
                            Observable.just(OnCityIdChanged(cityId))
                        )
                    }
            }
        }
    }

    private fun handleRetryLoading(): EventsMapSideEffect {
        return { actions, state ->
            actions.ofType<OnRetryLoading>().switchMap {
                getMapItems(state().cityId, state().renderType)
                    .subscribeOn(io())
                    .map<EventsMapAction>(::OnMapItemsLoaded)
                    .startWith(StartLoading)
                    .onErrorReturnItem(ErrorLoading)
            }
        }
    }

    private fun handleRenderTypeChanges(): EventsMapSideEffect {
        return { actions, state ->
            actions.ofType<OnRenderTypeChanged>().switchMap {
                getMapItems(state().cityId, it.renderType)
                    .subscribeOn(io())
                    .map<EventsMapAction>(::OnMapItemsLoaded)
                    .startWith(StartLoading)
                    .onErrorReturnItem(ErrorLoading)
            }
        }
    }

}
