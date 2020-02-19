package com.lounah.vkmc.feature.feature_market.markets.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.lounah.vkmc.core.di.ComponentStorage.getComponent
import com.lounah.vkmc.core.extensions.disposeOnDestroy
import com.lounah.vkmc.core.extensions.subscribeTo
import com.lounah.vkmc.core.recycler.paging.core.pagedScrollListener
import com.lounah.vkmc.feature.feature_market.R
import com.lounah.vkmc.feature.feature_market.markets.di.MarketsComponent
import com.lounah.vkmc.feature.feature_market.markets.presentation.MarketsAction
import com.lounah.vkmc.feature.feature_market.markets.presentation.MarketsAction.*
import com.lounah.vkmc.feature.feature_market.markets.presentation.MarketsFragmentPresenter
import com.lounah.vkmc.feature.feature_market.markets.presentation.MarketsState
import com.lounah.vkmc.feature.feature_market.markets.ui.recycler.MarketUi
import com.lounah.vkmc.feature.feature_market.markets.ui.recycler.MarketsAdapter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_markets.*
import kotlin.LazyThreadSafetyMode.*

internal class MarketsFragment : Fragment() {

    private val marketsAdapter: MarketsAdapter by lazy(NONE) {
        MarketsAdapter(::onMarketClicked, ::onRepeatPagedLoading)
    }

    private val presenter: MarketsFragmentPresenter by lazy(NONE) {
        getComponent<MarketsComponent>().presenter
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_markets, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecycler()
        initBindings()
        toolbar.setOnClickListener { recyclerView.smoothScrollToPosition(0) }
    }

    private fun initBindings() {
        presenter.state
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeTo(onNext = ::render)
            .disposeOnDestroy(this)
    }

    private fun render(state: MarketsState) {
        marketsAdapter.setItems(state.markets)
        title.text = getString(R.string.shops_in, state.city)
    }

    private fun initRecycler() {
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = marketsAdapter
        recyclerView.pagedScrollListener { presenter.input.accept(OnNextPage(it)) }
    }

    private fun onMarketClicked(market: MarketUi) {

    }

    private fun onRepeatPagedLoading() {
        presenter.input.accept(OnRetryLoadingClicked)
    }
}
