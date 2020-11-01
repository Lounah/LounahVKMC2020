package com.lounah.vkmc.feature_places.places.map.presentation

import com.google.android.gms.maps.model.LatLng
import com.lounah.vkmc.feature_places.places.map.domain.CityId

sealed class PlacesCommand {
    class LoadStoriesByLocation(
        val location: LatLng,
        val cityId: CityId,
        val force: Boolean = false
    ) : PlacesCommand()

    internal sealed class PlacesNews : PlacesCommand() {
        object ShowError : PlacesNews()
        class ShowPreviewDialog(val cityId: CityId) : PlacesNews()
    }
}
