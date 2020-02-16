package com.lounah.vkmc.di

import android.content.Context
import com.lounah.vkmc.feature.challenge_feature.di.ChallengeFeatureComponent
import com.lounah.vkmc.feature.challenge_feature.di.ChallengeFeatureDependencies
import com.lounah.vkmc.feature.feature_image_picker.di.ImagePickerComponent
import com.lounah.vkmc.feature.feature_image_picker.di.ImagePickerDependencies

interface AppComponent : ChallengeFeatureDependencies, ImagePickerDependencies {

    companion object {
        operator fun invoke(context: Context): AppComponent {
            return object : AppComponent {
                override val appContext: Context
                    get() = context
            }
        }
    }

    fun challengeFeatureComponent(): ChallengeFeatureComponent {
        return ChallengeFeatureComponent(this)
    }

    fun imagePickerComponent(): ImagePickerComponent {
        return ImagePickerComponent(this)
    }
}
