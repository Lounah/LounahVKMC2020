package com.lounah.vkmc.core.core_navigation.commands

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import com.lounah.vkmc.core.core_navigation.core.Command
import com.lounah.vkmc.core.core_navigation.core.Seed
import java.util.*

class SwitchActivityCommand(
    private val target: AppCompatActivity,
    private val containerId: Int,
    private val roots: Stack<Seed>,
    private val rootCommandDelegate: Command
) : Command by rootCommandDelegate {

    override fun invoke(containerId: Int, fragmentManager: FragmentManager) {
        roots.push(Seed(target.supportFragmentManager, containerId))
        rootCommandDelegate(this@SwitchActivityCommand.containerId, target.supportFragmentManager)
    }
}
