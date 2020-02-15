package com.lounah.vkmc.core.core_navigation.commands

import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.lounah.vkmc.core.core_navigation.core.Command
import com.lounah.vkmc.core.core_navigation.core.Seed
import com.lounah.vkmc.core.core_navigation.ext.commit
import java.util.*

class BackCommand(
    private val roots: Stack<Seed>,
    private val hasDelayedResults: Boolean,
    private val backStackIsEmptyAction: () -> Unit,
    private val postResult: () -> Unit
) : Command {

    override fun invoke(containerId: Int, fragmentManager: FragmentManager) {
        fragmentManager.run {
            when {
                fragments.size == 1 && backStackEntryCount == 0 -> {
                    roots.pop()
                    backStackIsEmptyAction()
                }
                hasDelayedResults -> postResult()
                else -> {
                    commit(containerId) { beginTransaction() }
                    val fragment = fragments.lastOrNull()
                    if (fragment is DialogFragment) fragment.dismiss() else popBackStack()
                }
            }
        }
    }
}
