package com.lounah.vkmc.core.core_navigation.commands

import androidx.fragment.app.FragmentManager
import com.lounah.vkmc.core.core_navigation.core.Command


object EmptyCommand : Command {

    override fun invoke(containerId: Int, fragmentManager: FragmentManager) = Unit
}
