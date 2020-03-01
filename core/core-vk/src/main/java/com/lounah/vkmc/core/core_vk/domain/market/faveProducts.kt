package com.lounah.vkmc.core.core_vk.domain.market

import com.lounah.vkmc.core.core_vk.business.commands.market.VKAddProductToFaveCommand
import com.lounah.vkmc.core.core_vk.business.commands.market.VKGetFaveProducts
import com.lounah.vkmc.core.core_vk.business.commands.market.VKRemoveProductFromFaveCommand
import com.vk.api.sdk.VK
import io.reactivex.Completable
import io.reactivex.Single

typealias OwnerId = String
typealias ProductId = String
typealias IsAddedToFave = Boolean

class RemoveProductFromFave : (OwnerId, ProductId) -> Completable {

    override fun invoke(ownerId: OwnerId, productId: ProductId): Completable {
        return Completable.fromAction {
            VK.executeSync(VKRemoveProductFromFaveCommand(ownerId, productId))
        }
    }
}

class AddProductToFave : (OwnerId, ProductId) -> Completable {

    override fun invoke(ownerId: OwnerId, productId: ProductId): Completable {
        return Completable.fromAction {
            VK.executeSync(VKAddProductToFaveCommand(ownerId, productId))
        }
    }
}

class CheckIsAddedToFave : (ProductId) -> Single<Boolean> {

    override fun invoke(productId: ProductId): Single<Boolean> {
        return Single.create { emitter ->
            val faveProducts = VK.executeSync(VKGetFaveProducts())
            emitter.onSuccess(faveProducts.any { it.id == productId })
        }
    }
}

class ToggleFaveProductState(
    private val addToFave: (OwnerId, ProductId) -> Completable = AddProductToFave(),
    private val removeFromFave: (OwnerId, ProductId) -> Completable = RemoveProductFromFave()
) : (OwnerId, ProductId, IsAddedToFave) -> Completable {

    override fun invoke(
        ownerId: OwnerId,
        productId: ProductId,
        isAddedToFave: IsAddedToFave
    ): Completable {
        return if (isAddedToFave) removeFromFave(ownerId, productId) else addToFave(ownerId, productId)
    }
}
