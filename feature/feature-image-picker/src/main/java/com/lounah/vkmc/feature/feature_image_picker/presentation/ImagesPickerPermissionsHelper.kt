package com.lounah.vkmc.feature.feature_image_picker.presentation

import android.Manifest.permission.*
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import com.lounah.vkmc.feature.feature_image_picker.ui.ImagePickerActivity
import com.lounah.vkmc.feature.feature_image_picker.ui.PickerType


internal const val CAMERA_REQ_CODE = 101
internal const val GALLERY_REQ_CODE = 102

internal class ImagesPickerPermissionsHelper(
    private val activity: ImagePickerActivity,
    private val onCameraPermissionsGranted: () -> Unit,
    private val onGalleryPermissionsGranted: () -> Unit,
    private val onGalleryPermissionsDenied: () -> Unit
) {
    private val cameraPermissions = arrayOf(CAMERA, WRITE_EXTERNAL_STORAGE)
    private val galleryPermissions = arrayOf(READ_EXTERNAL_STORAGE)

    private var currentPickerType: PickerType = PickerType.GALLERY

    init {
        requestPermissions(currentPickerType)
    }

    fun requestPermissions(pickerType: PickerType) {
        currentPickerType = pickerType
        val permissions = if (pickerType == PickerType.CAMERA) cameraPermissions else galleryPermissions
        ActivityCompat.requestPermissions(activity, permissions, pickerType.permissionRequestCode)
    }

    fun dispatchPermissionResult(
        requestCode: Int,
        grantResults: IntArray
    ) {
        val permissionRequestCode = currentPickerType.permissionRequestCode
        val permissionGranted = checkPermissionResult(requestCode, permissionRequestCode, grantResults)

        when (permissionRequestCode) {
            CAMERA_REQ_CODE -> if (permissionGranted) onCameraPermissionsGranted()
            GALLERY_REQ_CODE -> {
                if (permissionGranted) onGalleryPermissionsGranted() else onGalleryPermissionsDenied()
            }
        }
    }

    private fun checkPermissionResult(
        requestCode: Int,
        permissionCode: Int,
        grantResults: IntArray
    ): Boolean {
        return requestCode == permissionCode &&
                grantResults.isNotEmpty() &&
                grantResults.all { it == PackageManager.PERMISSION_GRANTED }
    }
}
