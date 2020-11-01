package com.lounah.vkmc.feature_places.host

import android.Manifest.permission.*
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.PermissionChecker
import pub.devrel.easypermissions.AfterPermissionGranted
import pub.devrel.easypermissions.AppSettingsDialog
import pub.devrel.easypermissions.EasyPermissions
import pub.devrel.easypermissions.PermissionRequest

internal const val APP_PERMISSIONS_RC = 101
internal const val LOCATION_PERMISSION_RC = 121

internal class PermissionsHelper(
    private val hostActivity: VKMainActivity
) {

    private val appPermissions = arrayOf(
        CAMERA,
        WRITE_EXTERNAL_STORAGE,
        ACCESS_FINE_LOCATION,
        ACCESS_COARSE_LOCATION
    )

    fun dispatchPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        EasyPermissions.onRequestPermissionsResult(
            requestCode,
            permissions,
            grantResults,
            hostActivity
        )
    }

    fun handlePermissionsDenial(requestCode: Int, permissions: List<String>) {
        if (EasyPermissions.somePermissionPermanentlyDenied(hostActivity, permissions)) {
            if (requestCode == APP_PERMISSIONS_RC) hostActivity.showRationale()
            AppSettingsDialog.Builder(hostActivity).build().show()
        }
    }

    fun handleLocationPermission() {
        if (!isLocationGranted()) {
            hostActivity.showRationale()
            ActivityCompat.requestPermissions(
                hostActivity,
                arrayOf(ACCESS_FINE_LOCATION),
                LOCATION_PERMISSION_RC
            )
        } else {
            hostActivity.hideRationale()
        }
    }

    private fun isLocationGranted(): Boolean {
        return ContextCompat.checkSelfPermission(
            hostActivity,
            ACCESS_FINE_LOCATION
        ) == PermissionChecker.PERMISSION_GRANTED
    }

    fun handlePermissionsGrant(requestCode: Int, permissions: List<String>, action: () -> Unit) {
        val isAppPermissions = appPermissions.all(permissions::contains)
        val isLocationPerm = permissions.any { it == ACCESS_FINE_LOCATION }
        when (requestCode) {
            LOCATION_PERMISSION_RC -> if (isLocationPerm) action()
            APP_PERMISSIONS_RC -> if (isAppPermissions) action()
        }
    }

    fun onRationaleDenied(requestCode: Int) {
        if (requestCode == APP_PERMISSIONS_RC || requestCode == LOCATION_PERMISSION_RC) {
            hostActivity.showRationale()
        }
    }

    @AfterPermissionGranted(APP_PERMISSIONS_RC)
    fun requestAppPermissions(onGranted: () -> Unit) {
        checkPermissions(appPermissions, APP_PERMISSIONS_RC, onGranted)
    }

    private fun checkPermissions(
        perms: Array<String>,
        requestCode: Int,
        onGranted: () -> Unit
    ) {
        if (EasyPermissions.hasPermissions(hostActivity, *perms)) {
            onGranted()
        } else {
            hostActivity.showRationale()
            EasyPermissions.requestPermissions(
                PermissionRequest.Builder(hostActivity, requestCode, *perms).build()
            )
        }
    }
}
