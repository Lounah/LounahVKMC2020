package com.lounah.vkmc.feature_places.di

import android.location.Geocoder
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.lounah.vkmc.core.core_vk.domain.GetCityById
import com.lounah.vkmc.core.core_vk.domain.user.VKGetUser
import com.lounah.vkmc.feature_places.camera.domain.CreateThumbnail
import com.lounah.vkmc.feature_places.camera.domain.PublishStoryVideo
import com.lounah.vkmc.feature_places.camera.domain.StoryCreatorImpl
import com.lounah.vkmc.feature_places.camera.presentation.PublishStoryCommandHandler
import com.lounah.vkmc.feature_places.camera.presentation.StorySettingsDialogStore
import com.lounah.vkmc.feature_places.places.feed.details.domain.GetStoryById
import com.lounah.vkmc.feature_places.places.feed.details.domain.IncrementStoryViews
import com.lounah.vkmc.feature_places.places.map.business.StoriesApiImpl
import com.lounah.vkmc.feature_places.places.map.business.StoriesMetadataParser
import com.lounah.vkmc.feature_places.places.map.domain.GetNearestCity
import com.lounah.vkmc.feature_places.places.map.domain.StoriesMapper
import com.lounah.vkmc.feature_places.places.map.domain.StoriesRepositoryImpl
import com.lounah.vkmc.feature_places.places.map.presentation.ClipsPlacesStore
import com.lounah.vkmc.feature_places.places.map.presentation.cmdhandler.LoadStoriesByLocationCommandHandler
import com.lounah.vkmc.feature_places.places.preview.domain.GetStoryPreviews
import com.lounah.vkmc.feature_places.places.preview.domain.StoryPreviewMapper
import com.lounah.vkmc.feature_places.places.preview.presentation.StoriesFeedPresenter
import java.util.*

interface ClipsPlacesModule {
    val store: ClipsPlacesStore
    val storySettingsStore: StorySettingsDialogStore
    val storiesDialogPresenter: StoriesFeedPresenter
}

@Suppress("FunctionName")
fun ClipsPlacesModule(deps: ClipsPlacesDependencies) = object : ClipsPlacesModule {

    private val storageRef = Firebase.storage("gs://vkmc2020.appspot.com/").reference
    private val storiesApi = StoriesApiImpl(
        storageReference = storageRef,
        storiesCollection = Firebase.firestore.collection("stories"),
        getNearestCity = GetNearestCity(),
        storiesMetadataParser = StoriesMetadataParser()
    )

    private val storiesRepository = StoriesRepositoryImpl(storiesApi)
    private val geoCoder = Geocoder(deps.appContext, Locale.getDefault())
    private val getCityById = GetCityById()

    private val getStoryPreviews =
        GetStoryPreviews(storiesRepository, StoryPreviewMapper(getCityById, geoCoder))

    private val getStoryById = GetStoryById(storiesRepository, geoCoder, getCityById)

    override val storiesDialogPresenter: StoriesFeedPresenter
        get() = StoriesFeedPresenter(
            getStoryPreviews,
            IncrementStoryViews(storiesRepository),
            getStoryById
        )

    private val loadStoriesCommandHandler: LoadStoriesByLocationCommandHandler
        get() = LoadStoriesByLocationCommandHandler(
            storiesRepository,
            GetNearestCity(),
            StoriesMapper
        )

    private val publishStoryCommandHandler: PublishStoryCommandHandler
        get() = PublishStoryCommandHandler(
            CreateThumbnail(deps.appContext, storageRef),
            StoryCreatorImpl(
                GetNearestCity(),
                VKGetUser(),
                PublishStoryVideo(storageRef),
                Firebase.firestore.collection("stories")
            )
        )

    override val store: ClipsPlacesStore
        get() = ClipsPlacesStore(loadStoriesCommandHandler)

    override val storySettingsStore: StorySettingsDialogStore
        get() = StorySettingsDialogStore(publishStoryCommandHandler)
}
