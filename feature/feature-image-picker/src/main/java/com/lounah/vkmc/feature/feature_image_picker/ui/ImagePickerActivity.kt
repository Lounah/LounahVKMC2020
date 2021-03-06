package com.lounah.vkmc.feature.feature_image_picker.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetBehavior.*
import com.lounah.vkmc.core.extensions.asType
import com.lounah.vkmc.feature.feature_image_picker.R
import kotlinx.android.synthetic.main.activity_image_picker.*
import kotlin.LazyThreadSafetyMode.NONE

class ImagePickerActivity : AppCompatActivity() {

    private val bottomSheetCallback = object : BottomSheetCallback() {
        override fun onSlide(bottomSheet: View, delta: Float) {
            backgroundDim.alpha = delta
        }

        override fun onStateChanged(bottomSheet: View, state: Int) {
            if (state == STATE_HIDDEN) finish()
        }
    }

    private val dialog: ImagePickerBottomSheet by lazy(NONE) {
        supportFragmentManager.findFragmentById(R.id.gallery).asType<ImagePickerBottomSheet>()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        window.setFlags(FLAG_LAYOUT_NO_LIMITS, FLAG_LAYOUT_NO_LIMITS)
        overridePendingTransition(R.anim.slide_bottom_up, R.anim.slide_stub)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image_picker)
        setUpBottomSheet()
    }

    private fun setUpBottomSheet() {
        dialog.run {
            BottomSheetBehavior.from(view)?.let { bsb ->
                bsb.state = STATE_EXPANDED
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
            Intent(context, ImagePickerActivity::class.java).apply {
                putExtra(EXTRA_REQUEST_CODE, requestCode)
            }.also { context.startActivityForResult(it, requestCode) }
        }
    }
}
