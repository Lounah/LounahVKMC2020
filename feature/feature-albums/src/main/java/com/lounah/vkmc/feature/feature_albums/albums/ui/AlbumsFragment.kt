package com.lounah.vkmc.feature.feature_albums.albums.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.GridLayoutManager.VERTICAL
import com.lounah.vkmc.core.di.ComponentStorage.getComponent
import com.lounah.vkmc.core.extensions.disposeOnDestroy
import com.lounah.vkmc.core.extensions.subscribeTo
import com.lounah.vkmc.core.extensions.toast
import com.lounah.vkmc.core.recycler.paging.core.pagedScrollListener
import com.lounah.vkmc.feature.feature_albums.OnBackPressedListener
import com.lounah.vkmc.feature.feature_albums.R
import com.lounah.vkmc.feature.feature_albums.albums.presentation.AlbumsAction
import com.lounah.vkmc.feature.feature_albums.albums.presentation.AlbumsAction.*
import com.lounah.vkmc.feature.feature_albums.albums.presentation.AlbumsEvent
import com.lounah.vkmc.feature.feature_albums.albums.presentation.AlbumsEvent.ErrorDeletingAlbum
import com.lounah.vkmc.feature.feature_albums.albums.presentation.AlbumsPresenter
import com.lounah.vkmc.feature.feature_albums.albums.presentation.AlbumsState
import com.lounah.vkmc.feature.feature_albums.albums.ui.recycler.AlbumsAdapter
import com.lounah.vkmc.feature.feature_albums.albums.ui.recycler.AlbumsSpanSizeLookUp
import com.lounah.vkmc.feature.feature_albums.albums.ui.recycler.GridSpacesDecoration
import com.lounah.vkmc.feature.feature_albums.albums.ui.recycler.holder.AlbumUi
import com.lounah.vkmc.feature.feature_albums.createalbum.ui.CreateAlbumFragment
import com.lounah.vkmc.feature.feature_albums.di.AlbumsComponent
import com.lounah.vkmc.feature.feature_albums.photos.ui.PhotosFragment
import io.reactivex.android.schedulers.AndroidSchedulers.mainThread
import kotlinx.android.synthetic.main.fragment_albums.*
import kotlin.LazyThreadSafetyMode.NONE

private const val TOOLBAR_FLIPPER_REGULAR = 0
private const val TOOLBAR_FLIPPER_EDIT_MODE = 1

class AlbumsFragment : Fragment(R.layout.fragment_albums), OnBackPressedListener {

    private val albumsAdapter: AlbumsAdapter by lazy(NONE) {
        AlbumsAdapter(::onAlbumClicked, ::onAlbumLongClicked, ::onDeleteClicked, ::onRepeatLoading)
    }

    private val presenter: AlbumsPresenter by lazy(NONE) {
        getComponent<AlbumsComponent>().albumsPresenter
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUi()
        initBindings()
    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        LoadAlbums.accept()
    }

    override fun onBackPressed(): Boolean {
        return if (toolbarFlipper.displayedChild == TOOLBAR_FLIPPER_EDIT_MODE) {
            OnCancelEditClicked.accept()
            true
        } else false
    }

    private fun initBindings() {
        presenter.state
            .observeOn(mainThread())
            .subscribeTo(onNext = ::render)
            .disposeOnDestroy(viewLifecycleOwner)

        presenter.events
            .observeOn(mainThread())
            .subscribeTo(onNext = ::handleEvent)
            .disposeOnDestroy(viewLifecycleOwner)

        LoadAlbums.accept()
    }

    private fun render(state: AlbumsState) {
        toolbarFlipper.displayedChild = if (state.inEditMode) {
            TOOLBAR_FLIPPER_EDIT_MODE
        } else {
            TOOLBAR_FLIPPER_REGULAR
        }
        albumsAdapter.setItems(state.albums)
    }

    private fun handleEvent(event: AlbumsEvent) {
        when (event) {
            is ErrorDeletingAlbum -> toast(R.string.could_not_delete_album)
        }
    }

    private fun initUi() {
        initClickListeners()
        initRecycler()
    }

    private fun initClickListeners() {
        addBtn.setOnClickListener { navigateTo(CreateAlbumFragment.newInstance()) }
        cancel.setOnClickListener { OnCancelEditClicked.accept() }
        editBtn.setOnClickListener { OnEditClicked.accept() }
    }

    private fun initRecycler() {
        val lm = GridLayoutManager(requireContext(), 2, VERTICAL, false)
        lm.spanSizeLookup = AlbumsSpanSizeLookUp { albumsAdapter.items }
        albums.layoutManager = lm
        albums.adapter = albumsAdapter
        albums.addItemDecoration(GridSpacesDecoration())
        albums.pagedScrollListener { OnNextPage(it).accept() }
    }

    private fun onAlbumClicked(album: AlbumUi) =
        navigateTo(PhotosFragment.newInstance(album.uid, album.title, album.size))

    private fun onAlbumLongClicked(album: AlbumUi) = OnEditClicked.accept()

    private fun onDeleteClicked(album: AlbumUi) = OnDeleteAlbumClicked(album.uid).accept()

    private fun onRepeatLoading() = OnRepeatLoadClicked.accept()

    private fun navigateTo(fragment: Fragment) {
        requireActivity().supportFragmentManager.beginTransaction()
            .add(R.id.container, fragment)
            .hide(this)
            .addToBackStack(null)
            .commit()
    }

    private fun AlbumsAction.accept() = presenter.input.accept(this)
}
