package com.lounah.vkmc.core.core_navigation.commands

import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.lounah.vkmc.core.core_navigation.core.Command

class ShowDialogCommand(private val dialog: DialogFragment) : Command {

    override fun invoke(containerId: Int, fragmentManager: FragmentManager) {
        dialog.show(fragmentManager, null)
    }
}
