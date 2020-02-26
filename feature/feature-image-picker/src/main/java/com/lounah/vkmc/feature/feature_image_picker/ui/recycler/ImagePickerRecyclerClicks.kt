package com.lounah.vkmc.feature.feature_image_picker.ui.recycler

import com.lounah.vkmc.core.recycler.base.BaseEvents
import com.lounah.vkmc.core.recycler.base.ViewTyped
import com.lounah.vkmc.feature.feature_image_picker.presentation.ImagePickerAction
import com.lounah.vkmc.feature.feature_image_picker.presentation.ImagePickerAction.OnImageSelected
import com.lounah.vkmc.feature.feature_image_picker.presentation.ImagePickerAction.OnPhotoPickerClicked
import io.reactivex.Observable
import io.reactivex.functions.Consumer

internal class ImagePickerRecyclerClicks(
    private val onPhotoPickerClicked: Observable<ViewTyped>,
    private val onPhotoSelected: Observable<ViewTyped>,
    override val input: Consumer<ImagePickerAction>
) : BaseEvents<ImagePickerAction>() {

    override fun bindInternal(): Observable<ImagePickerAction> {
        return Observable.merge(
            onPhotoPickerClicked.map { OnPhotoPickerClicked },
            onPhotoSelected.map { OnImageSelected(it.uid) }
        )
    }
}
