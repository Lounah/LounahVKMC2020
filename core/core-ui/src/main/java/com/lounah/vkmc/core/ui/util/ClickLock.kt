package com.lounah.vkmc.core.ui.util

import android.os.Handler
import android.os.Looper.getMainLooper
import android.view.View


private const val DEFAULT_TIMEOUT = 200L
private const val MSG_LOCK = 0

class ClickLock(private val defaultLockTime: Long = DEFAULT_TIMEOUT) {

    @get:Synchronized
    val isLocked: Boolean
        get() = lockHandler.hasMessages(MSG_LOCK)

    private val lockHandler = Handler(getMainLooper())

    @Synchronized
    fun checkAndMaybeLock(): Boolean {
        if (isLocked) return true
        lock()
        return false
    }

    @Synchronized
    fun lock() {
        lock(defaultLockTime)
    }

    @Synchronized
    fun lock(time: Long) {
        lockHandler.sendEmptyMessageDelayed(MSG_LOCK, time)
    }

    @Synchronized
    fun unlock() {
        lockHandler.removeMessages(MSG_LOCK)
    }

}

fun View.throttledClick(clickLock: ClickLock, onClick: () -> Unit) {
    setOnClickListener { if (!clickLock.checkAndMaybeLock()) onClick() }
}
