package com.lounah.vkmc.feature_places.places.map.domain

import android.location.Location

internal class GetDistance : (Location, Location) -> Int {

    override fun invoke(a: Location, b: Location): Int {
        return a.distanceTo(b).toInt() / 1000
    }
}
