package com.lounah.vkmc.core.core_navigation.commands

import androidx.fragment.app.FragmentManager
import com.lounah.vkmc.core.core_navigation.core.Command
import com.lounah.vkmc.core.core_navigation.core.DelayedResult
import java.util.*


class PostResultCommand(
    private val requestCode: Int,
    private val result: Any,
    private val delayedResults: Stack<DelayedResult>
) : Command {

    override fun invoke(containerId: Int, fragmentManager: FragmentManager) {
        delayedResults.addElement(DelayedResult(requestCode, result))
    }
}
