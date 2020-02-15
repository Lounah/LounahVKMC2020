@file:Suppress("NOTHING_TO_INLINE")

package com.lounah.vkmc.core.extensions

import android.app.Activity
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

inline fun AppCompatActivity.toast(msg: Int, length: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(this, msg, length).show()
}