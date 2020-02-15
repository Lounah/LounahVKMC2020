package com.lounah.vkmc.core.core_navigation.commands

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.lounah.vkmc.core.core_navigation.core.Command
import com.lounah.vkmc.core.core_navigation.core.Seed
import com.lounah.vkmc.core.core_navigation.ext.commit
import java.util.*


class RootCommand(
    private val fragment: Fragment,
    private val roots: Stack<Seed>
) : Command {

    override fun invoke(containerId: Int, fragmentManager: FragmentManager) {
        addRootIfNeeded(containerId, fragmentManager)
        fragmentManager.commit(containerId) {
            beginTransaction()
                .replace(containerId, fragment)
        }
    }

    private fun addRootIfNeeded(containerId: Int, fragmentManager: FragmentManager) {
        if (roots.find { it.containerId == containerId } == null) {
            roots.push(Seed(fragmentManager, containerId))
        }
    }
}
