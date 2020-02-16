package com.lounah.vkmc.feature.feature_image_picker.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.lounah.vkmc.core.extensions.asType
import com.lounah.vkmc.feature.feature_image_picker.R
import kotlinx.android.synthetic.main.activity_test.*
import kotlin.LazyThreadSafetyMode.NONE

class ImageViewerActivity : AppCompatActivity() {

    private val bottomSheetCallback = object : BottomSheetBehavior.BottomSheetCallback() {
        override fun onSlide(bottomSheet: View, delta: Float) {
            backgroundDim.alpha = delta
        }

        override fun onStateChanged(bottomSheet: View, state: Int) {
            if (state == BottomSheetBehavior.STATE_HIDDEN) finish()
        }
    }

    private val dialog: ImagePickerBottomSheet by lazy(NONE) {
        supportFragmentManager.findFragmentById(R.id.gallery).asType<ImagePickerBottomSheet>()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        overridePendingTransition(R.anim.slide_bottom_up, R.anim.slide_stub)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test)
        setUpBottomSheet()
    }

    private fun setUpBottomSheet() {
        dialog.run {
            BottomSheetBehavior.from(view)?.let { bsb ->
                bsb.state = BottomSheetBehavior.STATE_EXPANDED
                bsb.setBottomSheetCallback(bottomSheetCallback)
            }
            pickRequestCode = intent.getIntExtra(EXTRA_REQUEST_CODE, 0)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        dialog.onActivityResult(requestCode, resultCode, data)
    }

    override fun onDestroy() {
        super.onDestroy()
        overridePendingTransition(R.anim.slide_stub, R.anim.slide_bottom_down)
    }

    companion object {
        const val EXTRA_PICKED_IMAGE = "EXTRA_PICKED_IMAGE"
        private const val EXTRA_REQUEST_CODE = "EXTRA_REQUEST_CODE"

        fun start(context: Activity, requestCode: Int) {
            Intent(context, ImageViewerActivity::class.java).apply {
                putExtra(EXTRA_REQUEST_CODE, requestCode)
            }.also { context.startActivityForResult(it, requestCode) }
        }
    }
}