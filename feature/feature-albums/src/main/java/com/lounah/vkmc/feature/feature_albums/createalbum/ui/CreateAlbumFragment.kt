package com.lounah.vkmc.feature.feature_albums.createalbum.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.lounah.vkmc.core.di.ComponentStorage.getComponent
import com.lounah.vkmc.core.extensions.*
import com.lounah.vkmc.feature.feature_albums.R
import com.lounah.vkmc.feature.feature_albums.createalbum.presentation.CreateAlbumAction
import com.lounah.vkmc.feature.feature_albums.createalbum.presentation.CreateAlbumAction.*
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
        initListeners()
        title.showForceKeyboard()
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
        when {
            state.isLoading -> {
                checkmark.animateScale(0)
                progress.animateScale(1)
            }
            state.canCreateAlbum -> {
                progress.animateScale(0)
                checkmark.animateScale(1)
            }
            else -> {
                progress.animateScale(0)
                checkmark.animateScale(0)
            }
        }
    }

    private fun handleEvent(event: CreateAlbumEvent) {
        when (event) {
            is ShowError -> toast(R.string.could_not_create_album)
            is OnCreateSucceed -> performBack()
        }
    }

    private fun initListeners() {
        cancel.setOnClickListener { performBack() }
        title.onTextChange { OnTitleChanged(it).accept() }
        description.onTextChange { OnDescriptionChanged(it).accept() }
        switchPrivacy.setOnClickListener { OnPrivacyChanged(switchPrivacy.isChecked).accept() }
        confirmBtn.setOnClickListener {
            it.hideKeyboard()
            OnCreateClicked.accept()
        }
    }

    private fun performBack() {
        view!!.hideKeyboard()
        requireActivity().supportFragmentManager.popBackStack()
    }

    private fun CreateAlbumAction.accept() = presenter.input.accept(this)

    companion object {
        fun newInstance(): CreateAlbumFragment = CreateAlbumFragment()
    }
}
