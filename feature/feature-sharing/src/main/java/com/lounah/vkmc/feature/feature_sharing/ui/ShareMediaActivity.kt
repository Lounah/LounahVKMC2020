package com.lounah.vkmc.feature.feature_sharing.ui

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
import androidx.appcompat.app.AppCompatActivity
import com.lounah.vkmc.core.ui.util.ClickLock
import com.lounah.vkmc.core.ui.util.throttledClick
import com.lounah.vkmc.feature.feature_image_picker.ui.ImagePickerActivity
import com.lounah.vkmc.feature.feature_sharing.R
import com.vk.api.sdk.auth.VKScope
import kotlinx.android.synthetic.main.activity_challenge_feature.*

class ShareMediaActivity : AppCompatActivity() {

    private val clickLock = ClickLock()

    override fun onCreate(savedInstanceState: Bundle?) {
        window.addFlags(FLAG_LAYOUT_NO_LIMITS)
        overridePendingTransition(0, 0)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_challenge_feature)
        btnPicker.throttledClick(clickLock) {
            ImagePickerActivity.start(this, PICKER_RC)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICKER_RC && resultCode == Activity.RESULT_OK) {
            val selectedPic = data?.getStringExtra(ImagePickerActivity.EXTRA_PICKED_IMAGE).orEmpty()
            CreateWallPostBottomSheetDialog.newInstance(selectedPic)
                .show(supportFragmentManager, null)
        }
    }

    companion object {
        val authScopes = listOf(VKScope.WALL, VKScope.PHOTOS)

        private const val PICKER_RC = 101

        fun start(context: Context) =
            Intent(context, ShareMediaActivity::class.java).also(context::startActivity)
    }
}
