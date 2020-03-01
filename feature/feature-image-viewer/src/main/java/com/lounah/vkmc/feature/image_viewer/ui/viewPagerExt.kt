package com.lounah.vkmc.feature.image_viewer.ui

import androidx.viewpager.widget.ViewPager

fun ViewPager.pageChange(callback: (Int) -> Unit) {
    addOnPageChangeListener(object : ViewPager.SimpleOnPageChangeListener() {
        override fun onPageSelected(position: Int) {
            callback(position)
        }
    })
}