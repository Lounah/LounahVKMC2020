package com.lounah.vkmc.feature.feature_image_picker.ui

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.GridLayoutManager.SpanSizeLookup
import androidx.recyclerview.widget.RecyclerView
import com.lounah.vkmc.core.extensions.hide
import com.lounah.vkmc.core.extensions.show
import com.lounah.vkmc.core.recycler.Recycler
import com.lounah.vkmc.core.recycler.base.ViewTyped
import com.lounah.vkmc.feature.feature_image_picker.R
import com.lounah.vkmc.feature.feature_image_picker.presentation.CameraStarter
import com.lounah.vkmc.feature.feature_image_picker.presentation.ImagesPickerPermissionsHelper
import com.lounah.vkmc.feature.feature_image_picker.ui.viewholders.CameraPickerUi
import com.lounah.vkmc.feature.feature_image_picker.ui.viewholders.ImagePickerViewHolderFactory
import kotlinx.android.synthetic.main.activity_image_picker.*
import kotlin.LazyThreadSafetyMode.NONE

class ImagePickerActivity : AppCompatActivity() {

    private var requestCode: Int = 0
    private lateinit var pickerPermissionsHelper: ImagesPickerPermissionsHelper
    private lateinit var cameraStarter: CameraStarter

    private val recycler: Recycler<ViewTyped> by lazy(NONE) {
        Recycler<ViewTyped>(recyclerView, ImagePickerViewHolderFactory())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        overridePendingTransition(0, 0)
        super.onCreate(savedInstanceState)
        initUI()
        initPermissionsHelper()
    }

    private fun initPermissionsHelper() {
        pickerPermissionsHelper =
            ImagesPickerPermissionsHelper(
                this,
                onCameraPermissionsGranted = ::openCamera,
                onGalleryPermissionsGranted = ::openGalleryViewer,
                onGalleryPermissionsDenied = permissionsNotice::show
            )
    }

    private fun initUI() {
        setContentView(R.layout.activity_image_picker)
        initRecycler()
        initClickListeners()
        initBindings()
        recycler.setItems(listOf(CameraPickerUi))
    }

    private fun initRecycler() {
        val layoutManager = GridLayoutManager(this, 3, RecyclerView.VERTICAL, false).apply {
            spanSizeLookup = object : SpanSizeLookup() {
                override fun getSpanSize(position: Int): Int = if (position == 0) 3 else 1
            }
        }
        recyclerView.layoutManager = layoutManager
    }

    private fun initBindings() {
//        AdditionalOptionsEvents(
//            recycler.clickedItem(R.layout.digital_signature_item_additional_option),
//            presenter.input
//        ).bind(this)

        /*
        internal class AdditionalOptionsEvents(
    private val onAdditionalOptionClick: Observable<ViewTyped>,
    override val input: Consumer<AdditionalOptionsAction>
) : BaseEvents<AdditionalOptionsAction>() {

    override fun bindInternal(): Observable<AdditionalOptionsAction> {
        return onAdditionalOptionClick.map { OnAdditionalOptionClicked(it.uid) }
    }
}

         */
    }

    private fun initClickListeners() {
        btnRepeat.setOnClickListener {
            pickerPermissionsHelper.requestPermissions(PickerType.GALLERY)
        }
        close.setOnClickListener {
            setResult(Activity.RESULT_CANCELED)
            finish()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) = pickerPermissionsHelper.dispatchPermissionResult(requestCode, grantResults)

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == this.requestCode && resultCode == Activity.RESULT_OK) {

        } else {
            setResult(Activity.RESULT_CANCELED)
            finish()
        }
    }

    private fun openGalleryViewer() {
        permissionsNotice.hide()
    }

    private fun openCamera() {
        requestCode = intent.getIntExtra(EXTRA_REQUEST_CODE, 0)
        cameraStarter = CameraStarter()
        cameraStarter(this, requestCode)
    }

    companion object {
        private const val EXTRA_REQUEST_CODE = "EXTRA_REQUEST_CODE"

        fun start(context: Context, requestCode: Int) {
            Intent(context, ImagePickerActivity::class.java).apply {
                putExtra(EXTRA_REQUEST_CODE, requestCode)
            }.also(context::startActivity)
        }
    }
}
