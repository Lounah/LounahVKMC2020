package com.lounah.vkmc.feature.image_viewer.ui

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.lounah.vkmc.core.core_vk.domain.AlbumId
import com.lounah.vkmc.core.core_vk.domain.Offset
import com.lounah.vkmc.core.extensions.animateAlpha
import com.lounah.vkmc.core.extensions.disposeOnDestroy
import com.lounah.vkmc.core.extensions.getOrThrow
import com.lounah.vkmc.core.extensions.subscribeTo
import com.lounah.vkmc.feature.image_viewer.R
import com.lounah.vkmc.feature.image_viewer.presentation.ImageViewerPresenter
import io.reactivex.android.schedulers.AndroidSchedulers.mainThread
import kotlinx.android.synthetic.main.activity_image_viewer.*
import kotlin.LazyThreadSafetyMode.NONE

class ImageViewerActivity : AppCompatActivity() {

    private val presenter by lazy(NONE) {
        val albumId = intent.extras!!.getString(ARG_ALBUM_ID).orEmpty()
        ImageViewerPresenter(albumId)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        overridePendingTransition(0, 0)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image_viewer)
        intent.extras?.run {
            val images = getStringArrayList(ARG_IMAGES)
            val image = getString(ARG_PICKER_IMG)
            val startFrom = getInt(ARG_START_FROM)
            val asVkViewer = getBoolean(ARG_AS_VK_VIEWER)
            val albumSize = getInt(ARG_ALBUM_SIZE)
            initUi(images, image, startFrom, asVkViewer, albumSize)
        }
    }

    override fun finish() {
        super.finish()
        overridePendingTransition(0, 0)
    }

    private fun initUi(
        images: ArrayList<String>?,
        pickerImage: String?,
        startFrom: Int,
        asVkViewer: Boolean,
        albumSize: Int
    ) {
        val isForSinglePick = pickerImage != null && !asVkViewer
        val uiHelper = ImageViewerUiHelper(this, isForSinglePick)
        val onMove = uiHelper::handleImageMove
        val onDismiss = uiHelper::handleImageDismiss
        val onClick = uiHelper::onImageClicked
        if (isForSinglePick) buttonContainer.animateAlpha(1, 450)
        val pagerImages = if (!isForSinglePick) images.getOrThrow() else arrayListOf(pickerImage!!)
        val adapter = ImageViewerViewPagerAdapter( this, onClick, onMove, onDismiss)
        adapter.addImages(pagerImages)
        viewPager.adapter = adapter
        viewPager.currentItem = startFrom

        if (asVkViewer) {
            viewerTitle.text = resources.getString(R.string.offset_counter, startFrom + 1, albumSize)
            viewPager.pageChange { offset ->
                viewerTitle.text = resources.getString(R.string.offset_counter, offset + 1, albumSize)
                if (offset % 49 == 0 && offset != 0) requestPhotos(offset, adapter)
            }

        }

        initClickListeners()
    }

    private fun requestPhotos(offset: Offset, adapter: ImageViewerViewPagerAdapter) {
        presenter.requestPhotos(offset)
            .observeOn(mainThread())
            .subscribeTo { adapter.addImages(it) }
            .disposeOnDestroy(this)
    }

    private fun initClickListeners() {
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
        private const val ARG_ALBUM_SIZE = "arg_album_size"
        private const val ARG_AS_VK_VIEWER = "arg_as_vk_viewer"
        private const val ARG_ALBUM_ID = "arg_album_id"

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

        fun startAsVkViewer(
            context: Activity,
            prevPhotos: ArrayList<String>,
            image: String,
            index: Int,
            albumSize: Int,
            albumId: AlbumId
        ) {
            Intent(context, ImageViewerActivity::class.java)
                .apply {
                    val bundle = Bundle().apply {
                        putString(ARG_PICKER_IMG, image)
                        putString(ARG_ALBUM_ID, albumId)
                        putStringArrayList(ARG_IMAGES, prevPhotos)
                        putInt(ARG_START_FROM, index)
                        putInt(ARG_ALBUM_SIZE, albumSize)
                        putBoolean(ARG_AS_VK_VIEWER, true)
                    }
                    putExtras(bundle)
                }.also(context::startActivity)
        }
    }
}
