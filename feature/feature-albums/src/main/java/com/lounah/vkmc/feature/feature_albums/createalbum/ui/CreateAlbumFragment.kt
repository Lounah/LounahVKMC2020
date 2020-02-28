package com.lounah.vkmc.feature.feature_albums.createalbum.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.lounah.vkmc.core.di.ComponentStorage.getComponent
import com.lounah.vkmc.core.extensions.animateScale
import com.lounah.vkmc.core.extensions.disposeOnDestroy
import com.lounah.vkmc.core.extensions.subscribeTo
import com.lounah.vkmc.core.extensions.toast
import com.lounah.vkmc.feature.feature_albums.R
import com.lounah.vkmc.feature.feature_albums.createalbum.presentation.CreateAlbumEvent
import com.lounah.vkmc.feature.feature_albums.createalbum.presentation.CreateAlbumEvent.OnCreateSucceed
import com.lounah.vkmc.feature.feature_albums.createalbum.presentation.CreateAlbumEvent.ShowError
import com.lounah.vkmc.feature.feature_albums.createalbum.presentation.CreateAlbumState
import com.lounah.vkmc.feature.feature_albums.di.AlbumsComponent
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.fragment_create_album.*
import kotlin.LazyThreadSafetyMode.NONE

internal class CreateAlbumFragment : Fragment() {

    private val presenter by lazy(NONE) {
        getComponent<AlbumsComponent>().createAlbumPresenter
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_create_album, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initBindings()
    }

    private fun initBindings() {
        presenter.state
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeTo(onNext = ::render)
            .disposeOnDestroy(viewLifecycleOwner)

        presenter.events
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeTo(onNext = ::handleEvent)
            .disposeOnDestroy(viewLifecycleOwner)
    }

    private fun render(state: CreateAlbumState) {
        if (state.isLoading) {
            checkmark.animateScale(0)
            progress.animateScale(1)
        } else {
            if (state.canCreateAlbum)
                checkmark.animateScale(1)
            progress.animateScale(0)
        }
    }

    private fun handleEvent(event: CreateAlbumEvent) {
        when (event) {
            is ShowError -> toast(R.string.could_not_create_album)
            is OnCreateSucceed -> requireActivity().supportFragmentManager.popBackStack()
        }
    }

    companion object {
        fun newInstance(): CreateAlbumFragment = CreateAlbumFragment()
    }
}
