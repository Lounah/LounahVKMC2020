package com.lounah.vkmc.feature_places.host

import android.content.Context
import android.content.Intent
import android.content.Intent.CATEGORY_DEFAULT
import android.net.Uri
import android.os.Bundle
import android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS
import androidx.appcompat.app.AppCompatActivity
import com.lounah.vkmc.core.extensions.hide
import com.lounah.vkmc.core.extensions.show
import com.lounah.vkmc.core.extensions.transparentStatusbar
import com.lounah.vkmc.feature.feature_places.R
import com.lounah.vkmc.feature_places.host.MainViewPagerAdapter.Companion.POSITION_ALBUMS
import com.lounah.vkmc.feature_places.host.MainViewPagerAdapter.Companion.POSITION_CAMERA
import com.lounah.vkmc.feature_places.host.MainViewPagerAdapter.Companion.POSITION_CONTENT
import kotlinx.android.synthetic.main.activity_vkmc_main.*
import pub.devrel.easypermissions.EasyPermissions

class VKMainActivity : AppCompatActivity(R.layout.activity_vkmc_main),
    EasyPermissions.PermissionCallbacks,
    EasyPermissions.RationaleCallbacks {

    companion object {
        fun start(context: Context) {
            Intent(context, VKMainActivity::class.java)
                .also(context::startActivity)
        }
    }

    private val permissionsHelper = PermissionsHelper(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initUi()
        requestPermissions()
    }

    override fun onBackPressed() {
        if (pager.currentItem == POSITION_CAMERA) {
            pager.currentItem = POSITION_CONTENT
        } else {
            super.onBackPressed()
        }
    }

    private fun initUi() {
        setupViewPager()
        transparentStatusbar()
        btnRepeat.setOnClickListener {
            Intent().apply {
                action = ACTION_APPLICATION_DETAILS_SETTINGS
                addCategory(CATEGORY_DEFAULT)
                data = Uri.parse("package:$packageName")
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }.also(::startActivity)
        }
    }

    private fun requestPermissions() {
        permissionsHelper.handleLocationPermission()
        permissionsHelper.requestAppPermissions { hideRationale() }
    }

    private fun setupViewPager() {
        with(pager) {
            adapter = MainViewPagerAdapter(supportFragmentManager)
            offscreenPageLimit = 1
            currentItem = POSITION_CONTENT
        }
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
        permissionsHelper.handlePermissionsGrant(requestCode, perms, ::hideRationale)
    }

    fun hideRationale() {
        permissionsNotice.hide()
        pager.show()
    }

    fun showRationale() {
        pager.hide()
        permissionsNotice.show()
    }

    fun navigateToPhotos() {
        pager.currentItem = POSITION_CAMERA
    }

    fun navigateToAlbums() {
        pager.currentItem = POSITION_ALBUMS
    }
}
