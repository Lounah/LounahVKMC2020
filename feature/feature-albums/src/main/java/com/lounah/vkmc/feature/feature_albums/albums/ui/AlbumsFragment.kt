package com.lounah.vkmc.feature.feature_albums.albums.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.GridLayoutManager.VERTICAL
import com.lounah.vkmc.core.di.ComponentStorage.getComponent
import com.lounah.vkmc.core.extensions.disposeOnDestroy
import com.lounah.vkmc.core.extensions.subscribeTo
import com.lounah.vkmc.core.recycler.paging.core.pagedScrollListener
import com.lounah.vkmc.feature.feature_albums.OnBackPressedListener
import com.lounah.vkmc.feature.feature_albums.R
import com.lounah.vkmc.feature.feature_albums.albums.presentation.AlbumsAction
import com.lounah.vkmc.feature.feature_albums.albums.presentation.AlbumsAction.*
import com.lounah.vkmc.feature.feature_albums.albums.presentation.AlbumsPresenter
import com.lounah.vkmc.feature.feature_albums.albums.presentation.AlbumsState
import com.lounah.vkmc.feature.feature_albums.albums.ui.recycler.AlbumsAdapter
import com.lounah.vkmc.feature.feature_albums.albums.ui.recycler.AlbumsSpanSizeLookUp
import com.lounah.vkmc.feature.feature_albums.albums.ui.recycler.GridSpacesDecoration
import com.lounah.vkmc.feature.feature_albums.albums.ui.recycler.holder.AlbumUi
import com.lounah.vkmc.feature.feature_albums.di.AlbumsComponent
import com.lounah.vkmc.feature.feature_albums.photos.ui.PhotosFragment
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.fragment_albums.*
import kotlin.LazyThreadSafetyMode.NONE

private const val TOOLBAR_FLIPPER_REGULAR = 0
private const val TOOLBAR_FLIPPER_EDIT_MODE = 1

internal class AlbumsFragment : Fragment(), OnBackPressedListener {

    private val albumsAdapter: AlbumsAdapter by lazy(NONE) {
        AlbumsAdapter(::onAlbumClicked, ::onAlbumLongClicked, ::onDeleteClicked, ::onRepeatLoading)
    }

    private val presenter: AlbumsPresenter by lazy(NONE) {
        getComponent<AlbumsComponent>().albumsPresenter
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_albums, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUi()
        initBindings()
    }

    override fun onBackPressed(): Boolean {
        return if (toolbarFlipper.displayedChild == TOOLBAR_FLIPPER_EDIT_MODE) {
            OnCancelEditClicked.accept()
            true
        } else false
    }

    private fun initBindings() {
        presenter.state
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeTo(onNext = ::render)
            .disposeOnDestroy(viewLifecycleOwner)
    }

    private fun render(state: AlbumsState) {
        toolbarFlipper.displayedChild = if (state.inEditMode) {
            TOOLBAR_FLIPPER_EDIT_MODE
        } else {
            TOOLBAR_FLIPPER_REGULAR
        }
        albumsAdapter.setItems(state.albums)
    }

    private fun initUi() {
        initClickListeners()
        initRecycler()
    }

    private fun initClickListeners() {
        add.setOnClickListener { }
        cancel.setOnClickListener { OnCancelEditClicked.accept() }
        edit.setOnClickListener { OnEditClicked.accept() }
    }

    private fun initRecycler() {
        val lm = GridLayoutManager(requireContext(), 2, VERTICAL, false)
        lm.spanSizeLookup = AlbumsSpanSizeLookUp { albumsAdapter.items }
        albums.layoutManager = lm
        albums.adapter = albumsAdapter
        albums.addItemDecoration(GridSpacesDecoration())
        albums.pagedScrollListener { OnNextPage(it).accept() }
    }

    private fun onAlbumClicked(album: AlbumUi) {
        requireActivity().supportFragmentManager.beginTransaction()
            .add(R.id.container, PhotosFragment.newInstance(album.uid, album.title))
            .hide(this)
            .addToBackStack(null)
            .commit()
    }

    private fun onAlbumLongClicked(album: AlbumUi) = OnEditClicked.accept()

    private fun onDeleteClicked(album: AlbumUi) {

    }

    private fun onRepeatLoading() = OnRepeatLoadClicked.accept()

    private fun AlbumsAction.accept() = presenter.input.accept(this)
}
