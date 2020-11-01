package com.lounah.vkmc.core.core_vk.domain.user

import com.lounah.vkmc.core.core_vk.business.commands.user.User
import com.lounah.vkmc.core.core_vk.business.commands.user.VKGetUserCommand
import com.vk.api.sdk.VK
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers.io

class VKGetUser : () -> Observable<User> {

    override fun invoke(): Observable<User> {
        return Observable.fromCallable {
            VK.executeSync(VKGetUserCommand())
        }.subscribeOn(io())
    }
}
