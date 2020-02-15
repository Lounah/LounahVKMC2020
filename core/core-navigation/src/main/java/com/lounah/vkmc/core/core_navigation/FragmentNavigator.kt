package com.lounah.vkmc.core.core_navigation

import com.lounah.vkmc.core.core_navigation.core.Navigator
import com.lounah.vkmc.core.core_navigation.core.Router

class FragmentNavigator : Navigator {
    private var internalRouter: Router? = null

    override val router: Router
        get() = internalRouter
            ?: error("Router is not initialized! Please, call [setRouter] first!")

    override fun setRouter(router: Router) {
        internalRouter = router
    }

    override fun clearRouter() {
        internalRouter = null
    }
}
