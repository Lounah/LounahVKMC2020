package com.lounah.vkmc.core.extensions

fun String.firstIndexOfDigit(): Int {
    for (i in indices) {
        if (this[i] in '0'..'9') {
            return i
        }
    }
    return -1
}
