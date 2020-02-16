package com.lounah.vkmc.di

import android.content.Context
import com.lounah.vkmc.feature.feature_image_picker.di.ImagePickerComponent
import com.lounah.vkmc.feature.feature_image_picker.di.ImagePickerDependencies
import com.lounah.vkmc.feature.feature_sharing.di.SharingComponent
import com.lounah.vkmc.feature.feature_sharing.di.SharingDependencies

interface AppComponent : SharingDependencies, ImagePickerDependencies {

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
}
