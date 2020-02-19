package com.lounah.vkmc.feature.feature_market.markets.ui

import android.app.Activity.*
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.lounah.vkmc.core.core_vk.domain.CityId
import com.lounah.vkmc.core.di.ComponentStorage.getComponent
import com.lounah.vkmc.core.extensions.disposeOnDestroy
import com.lounah.vkmc.core.extensions.subscribeTo
import com.lounah.vkmc.core.recycler.paging.core.pagedScrollListener
import com.lounah.vkmc.feature.feature_market.R
import com.lounah.vkmc.feature.feature_market.cities.ui.CitiesListFragment
import com.lounah.vkmc.feature.feature_market.markets.di.MarketsComponent
import com.lounah.vkmc.feature.feature_market.markets.presentation.MarketsAction.*
import com.lounah.vkmc.feature.feature_market.markets.presentation.MarketsFragmentPresenter
import com.lounah.vkmc.feature.feature_market.markets.presentation.MarketsState
import com.lounah.vkmc.feature.feature_market.markets.ui.recycler.MarketUi
import com.lounah.vkmc.feature.feature_market.markets.ui.recycler.MarketsAdapter
import io.reactivex.android.schedulers.AndroidSchedulers.*
import io.reactivex.schedulers.Schedulers.*
import kotlinx.android.synthetic.main.fragment_markets.*
import kotlin.LazyThreadSafetyMode.NONE

private const val SELECTED_CITY_RC = 101

internal class MarketsFragment : Fragment() {

    private val marketsAdapter: MarketsAdapter by lazy(NONE) {
        MarketsAdapter(::onMarketClicked, ::onRepeatPagedLoading)
    }

    private val presenter: MarketsFragmentPresenter by lazy(NONE) {
        getComponent<MarketsComponent>().presenter
    }

    private var selectedCityId: CityId = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_markets, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecycler()
        initBindings()
        initClickListeners()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == SELECTED_CITY_RC && resultCode == RESULT_OK) {
            val cityId = data?.getStringExtra(CitiesListFragment.SELECTED_CITY).orEmpty()
            selectedCityId = cityId
            presenter.input.accept(ChangeCityId(cityId))
            presenter.input.accept(OnCityIdChanged(cityId))
        }
    }

    private fun initBindings() {
        presenter.state
            .subscribeOn(io())
            .observeOn(mainThread())
            .subscribeTo(onNext = ::render)
            .disposeOnDestroy(this)
    }

    private fun render(state: MarketsState) {
        marketsAdapter.setItems(state.markets)
        selectedCityId = state.cityId
        title.text = getString(R.string.shops_in, state.city)
    }

    private fun initRecycler() {
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = marketsAdapter
        recyclerView.pagedScrollListener { presenter.input.accept(OnNextPage(it)) }
    }

    private fun onMarketClicked(market: MarketUi) {

    }

    private fun initClickListeners() {
        title.setOnClickListener {
            CitiesListFragment.newInstance(selectedCityId).apply {
                setTargetFragment(this@MarketsFragment, SELECTED_CITY_RC)
            }.show(activity!!.supportFragmentManager, null)
        }
        toolbar.setOnClickListener { recyclerView.smoothScrollToPosition(0) }
    }

    private fun onRepeatPagedLoading() {
        presenter.input.accept(OnRetryLoadingClicked)
    }
}
