package com.lounah.vkmc.feature_places.host

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.lounah.vkmc.feature.feature_places.R
import kotlinx.android.synthetic.main.fragment_vkmc_navigation.*

internal class VKNavigationFragment : Fragment(R.layout.fragment_vkmc_navigation) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpBottomNavigation()
    }

    private fun setUpBottomNavigation() {
        bottomNavigation.selectedItemId = R.id.tab_clips
        bottomNavigation.setOnNavigationItemSelectedListener { false }
    }
}
