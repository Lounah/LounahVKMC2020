package com.lounah.vkmc.feature.image_viewer.ui

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.lounah.vkmc.core.extensions.animateAlpha
import com.lounah.vkmc.core.extensions.getOrThrow
import com.lounah.vkmc.feature.image_viewer.R
import kotlinx.android.synthetic.main.activity_image_viewer.*

class ImageViewerActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        overridePendingTransition(0, 0)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image_viewer)
        intent.extras?.run {
            val images = getStringArrayList(ARG_IMAGES)
            val image = getString(ARG_PICKER_IMG)
            val startFrom = getInt(ARG_START_FROM)
            initUi(images, image, startFrom)
        }
    }

    override fun finish() {
        super.finish()
        overridePendingTransition(0, 0)
    }

    private fun initUi(
        images: ArrayList<String>?,
        pickerImage: String?,
        startFrom: Int
    ) {
        val isForSinglePick = pickerImage != null
        val uiHelper = ImageViewerUiHelper(this, isForSinglePick)
        val onMove = uiHelper::handleImageMove
        val onDismiss = uiHelper::handleImageDismiss
        val onClick = uiHelper::onImageClicked
        if (isForSinglePick) buttonContainer.animateAlpha(1, 450)
        val pagerImages = if (!isForSinglePick) images.getOrThrow() else arrayListOf(pickerImage!!)
        val adapter = ImageViewerViewPagerAdapter(pagerImages, this, onClick, onMove, onDismiss)
        viewPager.adapter = adapter
        viewPager.currentItem = startFrom
        back.setOnClickListener {
            setResult(Activity.RESULT_CANCELED)
            finish()
        }
        pickBtn.setOnClickListener {
            setResult(
                Activity.RESULT_OK,
                Intent().putExtra(PICKER_RESULT, intent.getStringExtra(ARG_PICKER_IMG))
            )
            finish()
        }

    }

    companion object {

        const val PICKER_RC = 116
        const val PICKER_RESULT = "PICKER_RESULT"
        private const val ARG_IMAGES = "arg_images"
        private const val ARG_PICKER_IMG = "arg_picker_image"
        private const val ARG_START_FROM = "arg_start_from"

        fun start(
            context: Context,
            images: ArrayList<String>,
            startFrom: Int = 0
        ) {
            Intent(context, ImageViewerActivity::class.java)
                .apply {
                    val bundle = Bundle().apply {
                        putStringArrayList(ARG_IMAGES, images)
                        putInt(ARG_START_FROM, startFrom)
                    }
                    putExtras(bundle)
                }.also(context::startActivity)
        }

        fun startAsPicker(
            context: Activity,
            image: String
        ) {
            Intent(context, ImageViewerActivity::class.java)
                .apply {
                    val bundle = Bundle().apply {
                        putString(ARG_PICKER_IMG, image)
                    }
                    putExtras(bundle)
                }.also { context.startActivityForResult(it, PICKER_RC) }
        }
    }
}
