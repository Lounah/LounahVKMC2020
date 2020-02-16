@file:Suppress("NOTHING_TO_INLINE")

package com.lounah.vkmc.core.extensions

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.provider.MediaStore
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

inline fun AppCompatActivity.toast(msg: Int, length: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(this, msg, length).show()
}

fun Activity.cameraIntent(code: Int, fileUri: Uri) {
    Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri)
        takePictureIntent.resolveActivity(packageManager)?.also {
            startActivityForResult(takePictureIntent, code)
        }
    }
}
