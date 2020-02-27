package com.lounah.vkmc.feature.feature_map.map.business

import android.location.Location

class GetDistance : (Location, Location) -> Int {

    override fun invoke(a: Location, b: Location): Int {
        return a.distanceTo(b).toInt() / 1000
    }
}
