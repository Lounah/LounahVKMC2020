package com.lounah.vkmc.feature_places.host

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.lounah.vkmc.feature_places.camera.ui.CameraFragment

internal class MainViewPagerAdapter(
    fm: FragmentManager
) : FragmentStatePagerAdapter(fm, BEHAVIOR_SET_USER_VISIBLE_HINT) {

    companion object {
        const val POSITION_CAMERA = 0
        const val POSITION_CONTENT = 1
        const val POSITION_ALBUMS = 2
    }

    override fun getCount() = 2

    override fun getItem(position: Int) = when (position) {
        POSITION_CAMERA -> CameraFragment()
        POSITION_CONTENT -> VKNavigationFragment()
        POSITION_ALBUMS -> Fragment()
        else -> error("Unknown position")
    }
}
