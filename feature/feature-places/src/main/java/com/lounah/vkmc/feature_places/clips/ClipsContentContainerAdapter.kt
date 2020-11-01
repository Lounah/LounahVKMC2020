package com.lounah.vkmc.feature_places.clips

import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.lounah.vkmc.feature.feature_places.R
import com.lounah.vkmc.feature_places.places.map.ui.ClipsPlacesFragment

internal class ClipsContentContainerAdapter(
    fm: FragmentManager,
    private val string: (Int) -> String
) : FragmentStatePagerAdapter(fm) {

    companion object {
        const val POSITION_FOR_YOU = 0
        const val POSITION_FOLLOWING = 1
        const val POSITION_TOP = 2
        const val POSITION_PLACES = 3
    }

    override fun getCount() = 4

    override fun getPageTitle(position: Int) = when (position) {
        POSITION_FOR_YOU -> string(R.string.for_u)
        POSITION_FOLLOWING -> string(R.string.following)
        POSITION_TOP -> string(R.string.top)
        POSITION_PLACES -> string(R.string.places)
        else -> error("Unknown position!")
    }


    override fun getItem(position: Int) = when (position) {
        POSITION_FOR_YOU -> ClipsStubFragment()
        POSITION_FOLLOWING -> ClipsStubFragment()
        POSITION_TOP -> ClipsStubFragment()
        POSITION_PLACES -> ClipsPlacesFragment()
        else -> error("Unknown position!")
    }
}
