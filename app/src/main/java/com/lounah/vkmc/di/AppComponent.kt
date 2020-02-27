package com.lounah.vkmc.di

import android.content.Context
import com.lounah.vkmc.feature.feature_albums.di.AlbumsComponent
import com.lounah.vkmc.feature.feature_albums.di.AlbumsDependencies
import com.lounah.vkmc.feature.feature_image_picker.di.ImagePickerComponent
import com.lounah.vkmc.feature.feature_image_picker.di.ImagePickerDependencies
import com.lounah.vkmc.feature.feature_map.di.EventsMapComponent
import com.lounah.vkmc.feature.feature_market.cities.di.CitiesListComponent
import com.lounah.vkmc.feature.feature_market.markets.di.MarketsComponent
import com.lounah.vkmc.feature.feature_market.productdetails.di.ProductDetailsComponent
import com.lounah.vkmc.feature.feature_market.productdetails.di.ProductDetailsDependencies
import com.lounah.vkmc.feature.feature_market.products.di.MarketGoodsComponent
import com.lounah.vkmc.feature.feature_sharing.di.SharingComponent
import com.lounah.vkmc.feature.feature_sharing.di.SharingDependencies
import com.lounah.vkmc.feature.feature_unsubscribe.di.UserGroupsComponent
import com.lounah.vkmc.feature.feature_unsubscribe.groupdetails.di.GroupDetailsComponent
import com.lounah.vkmc.feature.feature_unsubscribe.groupdetails.di.GroupDetailsDependencies

interface AppComponent : SharingDependencies, ImagePickerDependencies, GroupDetailsDependencies,
    ProductDetailsDependencies, AlbumsDependencies {

    companion object {
        operator fun invoke(context: Context): AppComponent {
            return object : AppComponent {
                override val appContext: Context
                    get() = context
            }
        }
    }

    fun challengeFeatureComponent(): SharingComponent {
        return SharingComponent(this)
    }

    fun imagePickerComponent(): ImagePickerComponent {
        return ImagePickerComponent(this)
    }

    fun userGroupsComponent(): UserGroupsComponent {
        return UserGroupsComponent()
    }

    fun groupDetailsComponent(): GroupDetailsComponent {
        return GroupDetailsComponent(this)
    }

    fun marketsComponent(): MarketsComponent {
        return MarketsComponent()
    }

    fun citiesListComponent(): CitiesListComponent {
        return CitiesListComponent()
    }

    fun goodsComponent(): MarketGoodsComponent {
        return MarketGoodsComponent()
    }

    fun productDetailsComponent(): ProductDetailsComponent {
        return ProductDetailsComponent(this)
    }

    fun albumsComponent(): AlbumsComponent {
        return AlbumsComponent(this)
    }

    fun eventsMapComponent(): EventsMapComponent {
        return EventsMapComponent()
    }
}
