package com.lounah.vkmc.feature.feature_sharing.ui

import android.os.Bundle
import android.view.View
import com.lounah.vkmc.core.core_vk.domain.wall.WallPost
import com.lounah.vkmc.core.di.ComponentStorage.getComponent
import com.lounah.vkmc.core.extensions.*
import com.lounah.vkmc.core.ui.bottomsheet.BaseBottomSheetDialogFragment
import com.lounah.vkmc.core.ui.bottomsheet.attachStickyFooter
import com.lounah.vkmc.core.ui.bottomsheet.bottomSheetBehaviour
import com.lounah.vkmc.core.ui.imageloader.load
import com.lounah.vkmc.feature.feature_sharing.R
import com.lounah.vkmc.feature.feature_sharing.di.SharingComponent
import com.lounah.vkmc.feature.feature_sharing.presentation.ShareMediaAction.*
import com.lounah.vkmc.feature.feature_sharing.presentation.ShareMediaEvent
import com.lounah.vkmc.feature.feature_sharing.presentation.ShareMediaEvent.OnPostSuccessfullyShared
import com.lounah.vkmc.feature.feature_sharing.presentation.ShareMediaEvent.ShowUploadingError
import com.lounah.vkmc.feature.feature_sharing.presentation.ShareMediaPresenter
import com.lounah.vkmc.feature.feature_sharing.presentation.ShareMediaState
import com.lounah.vkmc.feature.image_viewer.ui.ImageViewerActivity
import io.reactivex.android.schedulers.AndroidSchedulers.mainThread
import io.reactivex.schedulers.Schedulers.io
import kotlinx.android.synthetic.main.fragment_create_wall_post.*
import kotlin.LazyThreadSafetyMode.NONE

internal class CreateWallPostBottomSheetDialog :
    BaseBottomSheetDialogFragment(R.layout.fragment_create_wall_post) {

    private val presenter: ShareMediaPresenter by lazy(NONE) {
        getComponent<SharingComponent>().createWallPostFragmentPresenter
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initBindings()
        initUi()
    }

    private fun initUi() {
        val selectedPicPath = argsString(ARG_SELECTED_PIC)
        selectedImage.load(selectedPicPath)
        selectedImage.setOnClickListener {
            ImageViewerActivity.start(requireContext(), arrayListOf(selectedPicPath))
        }
        closeBtn.setOnClickListener { dismiss() }
        dialog?.attachStickyFooter(buttonContainer)
        inputComment.onTextChange { presenter.input.accept(OnTextInputChanged(it)) }
        actionButton.setOnClickListener {
            inputComment.hideKeyboard()
            presenter.input.accept(OnShareWallPostClicked)
        }
        presenter.input.accept(OnImageSelected(selectedPicPath))
    }

    private fun initBindings() {
        presenter.state
            .subscribeOn(io())
            .observeOn(mainThread())
            .subscribeTo(onNext = ::render)
            .disposeOnDestroy(this)

        presenter.events
            .subscribeOn(io())
            .observeOn(mainThread())
            .subscribeTo(onNext = ::handleEvent)
            .disposeOnDestroy(this)
    }

    private fun render(state: ShareMediaState) {
        if (state.isLoading) showProgress() else hideProgress()
        inputComment.isEnabled = !state.isLoading
        actionButton.isEnabled = !state.isLoading
        dialog.bottomSheetBehaviour.isHideable = !state.isLoading
    }

    private fun handleEvent(event: ShareMediaEvent) {
        when (event) {
            is OnPostSuccessfullyShared -> onPostSuccessfullyCreated(event.post)
            is ShowUploadingError -> {
                hideProgress()
                toast(R.string.error_sharing_photo)
            }
        }
    }

    private fun onPostSuccessfullyCreated(post: WallPost) {
        requireActivity().asType<ShareMediaActivity>().showWallPost(post)
        dismiss()
    }

    private fun showProgress() {
        actionButton.text = null
        progressBar.show()
    }

    private fun hideProgress() {
        progressBar.hide()
        actionButton.setText(R.string.send)
    }

    companion object {
        private const val ARG_SELECTED_PIC = "ARG_SELECTED_PIC"

        fun newInstance(selectedPicPath: String): CreateWallPostBottomSheetDialog {
            return CreateWallPostBottomSheetDialog()
                .apply {
                    arguments = Bundle().apply {
                        putString(ARG_SELECTED_PIC, selectedPicPath)
                    }
                }
        }
    }
}
