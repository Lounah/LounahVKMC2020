package com.lounah.vkmc.feature.feature_image_picker.presentation

import android.Manifest.permission.*
import com.lounah.vkmc.core.extensions.show
import com.lounah.vkmc.feature.feature_image_picker.R
import com.lounah.vkmc.feature.feature_image_picker.ui.ImagePickerBottomSheet
import kotlinx.android.synthetic.main.fragment_image_picker.*
import pub.devrel.easypermissions.AfterPermissionGranted
import pub.devrel.easypermissions.AppSettingsDialog
import pub.devrel.easypermissions.EasyPermissions


internal const val CAMERA_RC = 101
internal const val GALLERY_RC = 102

internal class PermissionsHelper(
    private val bottomSheet: ImagePickerBottomSheet
) {

    private val cameraPermissions = arrayOf(CAMERA, WRITE_EXTERNAL_STORAGE)
    private val galleryPermissions = arrayOf(READ_EXTERNAL_STORAGE)

    fun dispatchPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, bottomSheet)
    }

    fun handlePermissionsDenial(requestCode: Int, permissions: List<String>) {
        if (EasyPermissions.somePermissionPermanentlyDenied(bottomSheet, permissions)) {
            if (requestCode == GALLERY_RC) bottomSheet.permissionsNotice.show()
            AppSettingsDialog.Builder(bottomSheet).build().show()
        }
    }

    fun handlePermissionsGrant(permissions: List<String>, action: () -> Unit) {
        if (EasyPermissions.hasPermissions(bottomSheet.requireContext(), *permissions.toTypedArray())) {
            action()
        }
    }

    fun onRationaleDenied(requestCode: Int) {
        if (requestCode == GALLERY_RC) {
            bottomSheet.permissionsNotice.show()
        }
    }

    @AfterPermissionGranted(CAMERA_RC)
    fun requestCameraPermission(onGranted: () -> Unit) {
        val rationale = bottomSheet.getString(R.string.storage_access_permission_rationale)
        checkPermissions(cameraPermissions, rationale, CAMERA_RC, onGranted)
    }

    @AfterPermissionGranted(GALLERY_RC)
    fun requestGalleryPermission(onGranted: () -> Unit) {
        val rationale = bottomSheet.getString(R.string.storage_access_permission_rationale)
        checkPermissions(galleryPermissions, rationale, GALLERY_RC, onGranted)
    }

    private fun checkPermissions(
        perms: Array<String>,
        rationale: String,
        requestCode: Int,
        onGranted: () -> Unit
    ) {
        if (EasyPermissions.hasPermissions(bottomSheet.requireContext(), *perms)) {
            onGranted()
        } else {
            if (requestCode == GALLERY_RC) bottomSheet.permissionsNotice.show()
            EasyPermissions.requestPermissions(
                bottomSheet,
                rationale,
                requestCode,
                *perms
            )
        }
    }
}