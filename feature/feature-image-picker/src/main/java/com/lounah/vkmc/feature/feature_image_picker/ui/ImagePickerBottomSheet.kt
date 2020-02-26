package com.lounah.vkmc.feature.feature_image_picker.ui

import android.app.Activity
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.lounah.vkmc.core.di.ComponentStorage.getComponent
import com.lounah.vkmc.core.extensions.*
import com.lounah.vkmc.core.recycler.Recycler
import com.lounah.vkmc.core.recycler.base.ViewTyped
import com.lounah.vkmc.feature.feature_image_picker.R
import com.lounah.vkmc.feature.feature_image_picker.di.ImagePickerComponent
import com.lounah.vkmc.feature.feature_image_picker.presentation.*
import com.lounah.vkmc.feature.feature_image_picker.presentation.ImagePickerAction.RequestGalleryPhotos
import com.lounah.vkmc.feature.feature_image_picker.presentation.ImagePickerEvent.RequestCameraAccess
import com.lounah.vkmc.feature.feature_image_picker.presentation.ImagePickerEvent.ShowSelectedImage
import com.lounah.vkmc.feature.feature_image_picker.ui.ImagePickerActivity.Companion.EXTRA_PICKED_IMAGE
import com.lounah.vkmc.feature.feature_image_picker.ui.recycler.CameraPickerUi
import com.lounah.vkmc.feature.feature_image_picker.ui.recycler.ImagePickerRecyclerClicks
import com.lounah.vkmc.feature.feature_image_picker.ui.recycler.ImagePickerViewHolderFactory
import com.lounah.vkmc.feature.feature_image_picker.ui.util.GridSpacesDecoration
import com.lounah.vkmc.feature.feature_image_picker.ui.util.ImagePickerSpanSizeLookup
import com.lounah.vkmc.feature.feature_image_picker.ui.util.Value
import com.lounah.vkmc.feature.image_viewer.ui.ImageViewerActivity
import com.lounah.vkmc.feature.image_viewer.ui.ImageViewerActivity.Companion.PICKER_RC
import com.lounah.vkmc.feature.image_viewer.ui.ImageViewerActivity.Companion.PICKER_RESULT
import io.reactivex.android.schedulers.AndroidSchedulers.mainThread
import io.reactivex.schedulers.Schedulers.io
import io.reactivex.schedulers.Schedulers.trampoline
import kotlinx.android.synthetic.main.fragment_image_picker.*
import pub.devrel.easypermissions.EasyPermissions
import java.io.File
import kotlin.LazyThreadSafetyMode.NONE


internal class ImagePickerBottomSheet : BottomSheetDialogFragment(),
    EasyPermissions.PermissionCallbacks,
    EasyPermissions.RationaleCallbacks {

    var pickRequestCode: Int = 0

    private val component = getComponent<ImagePickerComponent>()
    private val cameraStarter: (Activity, Int) -> Unit by lazy(NONE) { component.cameraStarter }
    private val createdPhotoValue: Value<File> by lazy(NONE) { component.createdPhotoValue }
    private val presenter: ImagePickerPresenter by lazy(NONE) { component.presenter }

    private val recycler: Recycler<ViewTyped> by lazy(NONE) {
        Recycler<ViewTyped>(recyclerView, ImagePickerViewHolderFactory()) {
            itemDecoration = listOf(GridSpacesDecoration(4.dp(activity)))
        }
    }

    private val activity: Activity
        get() = requireActivity()

    private val permissionsHelper = PermissionsHelper(this)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_image_picker, container, false)


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUI()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        permissionsHelper.dispatchPermissionsResult(requestCode, permissions, grantResults)
    }

    override fun onRationaleDenied(requestCode: Int) =
        permissionsHelper.onRationaleDenied(requestCode)

    override fun onPermissionsDenied(requestCode: Int, perms: MutableList<String>) =
        permissionsHelper.handlePermissionsDenial(requestCode, perms)

    override fun onRationaleAccepted(requestCode: Int) = Unit

    override fun onPermissionsGranted(requestCode: Int, perms: MutableList<String>) {
        val action = if (requestCode == CAMERA_RC) ::openCamera else ::openGalleryViewer
        permissionsHelper.handlePermissionsGrant(requestCode, perms, action)
    }

    private fun initUI() {
        initRecycler()
        initClickListeners()
        initBindings()
        permissionsHelper.requestGalleryPermission(::openGalleryViewer)
    }

    private fun initRecycler() {
        val layoutManager = GridLayoutManager(activity, 3, RecyclerView.VERTICAL, false).apply {
            spanSizeLookup = ImagePickerSpanSizeLookup()
        }
        recyclerView.layoutManager = layoutManager
    }

    private fun initBindings() {
        ImagePickerRecyclerClicks(
            recycler.clickedItem(R.layout.item_image_picker_pick_from_camera),
            recycler.clickedItem(R.layout.item_image_picker_photo),
            presenter.input
        ).bind(this)

        presenter.state
            .observeOn(mainThread())
            .subscribeOn(trampoline())
            .subscribeTo(onNext = ::renderState)
            .disposeOnDestroy(this)

        presenter.events
            .subscribeOn(io())
            .observeOn(mainThread())
            .subscribeTo(onNext = ::handleEvent)
            .disposeOnDestroy(this)
    }

    private fun renderState(state: ImagePickerState) {
        if (state.galleryPhotos.isEmpty() && permissionsHelper.hasGalleryPermissions()) {
            emptyView.show()
        } else {
            emptyView.hide()
        }
        recycler.updateItems(listOf(CameraPickerUi) + state.galleryPhotos)
    }

    private fun handleEvent(event: ImagePickerEvent) {
        when (event) {
            is RequestCameraAccess -> permissionsHelper.requestCameraPermission(::openCamera)
            is ShowSelectedImage -> ImageViewerActivity.startAsPicker(activity, event.path)
        }
    }

    private fun initClickListeners() {
        btnRepeat.setOnClickListener {
            permissionsHelper.requestGalleryPermission(::openGalleryViewer)
        }
        close.setOnClickListener {
            activity.setResult(Activity.RESULT_CANCELED)
            activity.finish()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val result = if (requestCode == PICKER_RC) {
            data?.getStringExtra(PICKER_RESULT)
        } else {
            createdPhotoValue.get()?.absolutePath
        }
        if (resultCode == RESULT_OK) {
            activity.setResult(resultCode, Intent().putExtra(EXTRA_PICKED_IMAGE, result))
            activity.finish()
        }
    }

    private fun openGalleryViewer() {
        permissionsNotice.hide()
        presenter.input.accept(RequestGalleryPhotos)
    }

    private fun openCamera() {
        cameraStarter(activity, pickRequestCode)
    }
}
