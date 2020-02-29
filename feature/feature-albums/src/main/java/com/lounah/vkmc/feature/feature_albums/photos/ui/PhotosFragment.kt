package com.lounah.vkmc.feature.feature_albums.photos.ui

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.GridLayoutManager.VERTICAL
import com.lounah.vkmc.core.core_vk.domain.AlbumId
import com.lounah.vkmc.core.di.ComponentStorage.getComponent
import com.lounah.vkmc.core.extensions.animateScale
import com.lounah.vkmc.core.extensions.disposeOnDestroy
import com.lounah.vkmc.core.extensions.subscribeTo
import com.lounah.vkmc.core.recycler.paging.core.pagedScrollListener
import com.lounah.vkmc.feature.feature_albums.R
import com.lounah.vkmc.feature.feature_albums.di.AlbumsComponent
import com.lounah.vkmc.feature.feature_albums.photos.presentation.PhotosAction
import com.lounah.vkmc.feature.feature_albums.photos.presentation.PhotosAction.OnNextPage
import com.lounah.vkmc.feature.feature_albums.photos.presentation.PhotosAction.OnRepeatLoadClicked
import com.lounah.vkmc.feature.feature_albums.photos.presentation.PhotosPresenter
import com.lounah.vkmc.feature.feature_albums.photos.presentation.PhotosState
import com.lounah.vkmc.feature.feature_albums.photos.ui.recycler.PhotoUi
import com.lounah.vkmc.feature.feature_albums.photos.ui.recycler.PhotosAdapter
import com.lounah.vkmc.feature.feature_albums.photos.ui.recycler.PhotosItemDecoration
import com.lounah.vkmc.feature.feature_albums.photos.ui.recycler.PhotosSpanSizeLookUp
import com.lounah.vkmc.feature.feature_image_picker.ui.ImagePickerActivity
import com.lounah.vkmc.feature.image_viewer.ui.ImageViewerActivity
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.fragment_photos.*
import kotlin.LazyThreadSafetyMode.NONE

internal class PhotosFragment : Fragment() {

    private val photosAdapter: PhotosAdapter by lazy(NONE) {
        PhotosAdapter(::onPhotoClicked, ::onRepeatLoading)
    }

    private val presenter: PhotosPresenter by lazy(NONE) {
        val albumName = arguments!!.getString(ARG_ALBUM_NAME).orEmpty()
        val albumId = arguments!!.getString(ARG_ALBUM_ID).orEmpty()
        getComponent<AlbumsComponent>().photosPresenterFactory(albumName, albumId)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_photos, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUi()
        initBindings()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_RC && resultCode == RESULT_OK) {
            val selectedPic = data?.getStringExtra(ImagePickerActivity.EXTRA_PICKED_IMAGE).orEmpty()
        }
    }

    private fun initBindings() {
        presenter.state
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeTo(onNext = ::render)
            .disposeOnDestroy(viewLifecycleOwner)
    }

    private fun initUi() {
        initRecycler()
        addBtn.setOnClickListener {
            ImagePickerActivity.start(requireActivity(), PICK_IMAGE_RC)
        }
    }

    private fun initRecycler() {
        val lm = GridLayoutManager(requireContext(), 3, VERTICAL, false)
        lm.spanSizeLookup = PhotosSpanSizeLookUp { photosAdapter.items }
        photos.layoutManager = lm
        photos.adapter = photosAdapter
        photos.addItemDecoration(PhotosItemDecoration())
        photos.pagedScrollListener { OnNextPage(it).accept() }
    }

    private fun render(state: PhotosState) {
        photosAdapter.setItems(state.photos)
        if (state.albumId.toInt() > 0) add.animateScale(1)
    }

    private fun onPhotoClicked(photo: PhotoUi) {
        ImageViewerActivity.start(requireContext(), arrayListOf(photo.path))
    }

    private fun onRepeatLoading() = OnRepeatLoadClicked.accept()

    private fun PhotosAction.accept() = presenter.input.accept(this)

    companion object {
        private const val PICK_IMAGE_RC = 123
        private const val ARG_ALBUM_ID = "album_id"
        private const val ARG_ALBUM_NAME = "album_name"

        fun newInstance(albumId: AlbumId, albumName: String): PhotosFragment {
            return PhotosFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_ALBUM_ID, albumId)
                    putString(ARG_ALBUM_NAME, albumName)
                }
            }
        }
    }
}
