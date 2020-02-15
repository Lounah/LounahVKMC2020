package com.lounah.vkmc.core.core_navigation.ext

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction

fun FragmentManager.commit(containerId: Int,
                           action: FragmentManager.(Fragment?) -> FragmentTransaction
): Int {
    return findFragmentById(containerId)?.let {
        when {
            isStateSaved -> action(it).commitAllowingStateLoss()
            else -> action(it).commit()
        }
    } ?: action(null).commitAllowingStateLoss()
}
