package com.lounah.vkmc.core.core_navigation.commands

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.lounah.vkmc.core.core_navigation.core.Command
import com.lounah.vkmc.core.core_navigation.ext.commit

class ForwardCommand(private val target: Fragment,
                     private val onTransaction: FragmentTransaction.() -> FragmentTransaction = { this },
                     private val addToBackStack: Boolean,
                     private val needResult: Boolean) : Command {

    override fun invoke(containerId: Int, fragmentManager: FragmentManager) {
        fragmentManager.commit(containerId) {
            beginTransaction()
                .onTransaction()
                .apply {
                    if (addToBackStack) addToBackStack(null)
                    it?.let { if (needResult) hide(it) else detach(it) }
                    add(containerId, target)
                }
        }
    }
}
