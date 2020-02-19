package com.lounah.vkmc.feature.feature_market.markets.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.lounah.vkmc.feature.feature_market.R
import com.lounah.vkmc.feature.feature_market.markets.ui.recycler.MarketsAdapter

internal class MarketsFragment : Fragment() {

    private val adapter: MarketsAdapter by lazy(LazyThreadSafetyMode.NONE) {

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_markets, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecycler()
    }

    private fun initRecycler() {

    }
}
