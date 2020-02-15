package com.lounah.vkmc.core.core_navigation.commands

import androidx.fragment.app.FragmentManager
import com.lounah.vkmc.core.core_navigation.core.Command
import com.lounah.vkmc.core.core_navigation.core.RouterResults

class BackWithResultCommand(
    private val requestCode: Int,
    private val result: Any,
    private val backCommandDelegate: Command,
    private val routerResults: RouterResults
) :
    Command by backCommandDelegate {

    override fun invoke(containerId: Int, fragmentManager: FragmentManager) {
        backCommandDelegate(containerId, fragmentManager)
        routerResults[requestCode]?.lastOrNull()?.onResult(result)
    }
}
