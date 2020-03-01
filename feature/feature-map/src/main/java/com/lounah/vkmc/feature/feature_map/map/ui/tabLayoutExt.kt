package com.lounah.vkmc.feature.feature_map.map.ui

import android.view.ViewGroup
import com.google.android.material.tabs.TabLayout

internal fun TabLayout.tabsMargins(left: Int, top: Int, right: Int, bottom: Int) {
    val tabLayout = getChildAt(0) as ViewGroup
    for (i in 0 until tabCount) {
        (tabLayout.getChildAt(i).layoutParams as ViewGroup.MarginLayoutParams).apply {
            setMargins(left, top, right, bottom)
        }
    }
}

internal fun TabLayout.onTabSelected(callback: (Int) -> Unit) {
    addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
        override fun onTabSelected(tab: TabLayout.Tab) = callback(tab.position)
        override fun onTabUnselected(tab: TabLayout.Tab?) = Unit
        override fun onTabReselected(tab: TabLayout.Tab?) = Unit
    })
}
