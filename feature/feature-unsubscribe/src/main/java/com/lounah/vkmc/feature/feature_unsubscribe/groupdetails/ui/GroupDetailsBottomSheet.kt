package com.lounah.vkmc.feature.feature_unsubscribe.groupdetails.ui

import android.os.Bundle
import android.view.View
import android.widget.Button
import com.lounah.vkmc.core.core_vk.vkViewIntent
import com.lounah.vkmc.core.di.ComponentStorage.getComponent
import com.lounah.vkmc.core.extensions.disposeOnDestroy
import com.lounah.vkmc.core.extensions.gone
import com.lounah.vkmc.core.extensions.subscribeTo
import com.lounah.vkmc.core.ui.bottomsheet.BaseBottomSheetDialogFragment
import com.lounah.vkmc.core.ui.bottomsheet.attachStickyFooter
import com.lounah.vkmc.feature.feature_unsubscribe.R
import com.lounah.vkmc.feature.feature_unsubscribe.groupdetails.di.GroupDetailsComponent
import com.lounah.vkmc.feature.feature_unsubscribe.groupdetails.presentation.GroupDetailsAction.OnGoToGroupClicked
import com.lounah.vkmc.feature.feature_unsubscribe.groupdetails.presentation.GroupDetailsAction.OnRetryLoadClicked
import com.lounah.vkmc.feature.feature_unsubscribe.groupdetails.presentation.GroupDetailsEvent
import com.lounah.vkmc.feature.feature_unsubscribe.groupdetails.presentation.GroupDetailsEvent.OpenGroupDetails
import com.lounah.vkmc.feature.feature_unsubscribe.groupdetails.presentation.GroupDetailsPresenter
import com.lounah.vkmc.feature.feature_unsubscribe.groupdetails.presentation.GroupDetailsState
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_group_details.*
import kotlin.LazyThreadSafetyMode.NONE

private const val FLIPPER_ITEM_LOADING = 0
private const val FLIPPER_ITEM_ERROR = 1
private const val FLIPPER_ITEM_CONTENT = 2

internal class GroupDetailsBottomSheet :
    BaseBottomSheetDialogFragment(R.layout.fragment_group_details, expandByDefault = false) {

    private val presenter: GroupDetailsPresenter by lazy(NONE) {
        val groupId = arguments?.getString(ARG_GROUP_ID).orEmpty()
        getComponent<GroupDetailsComponent>().presenterCreator(groupId)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initBindings()
        initUi()
    }

    private fun initBindings() {
        presenter.state
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeTo(onNext = ::render)
            .disposeOnDestroy(this)

        presenter.events
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeTo(onNext = ::handleEvent)
            .disposeOnDestroy(this)
    }

    private fun initUi() {
        dialog?.attachStickyFooter(buttonContainer, dismissOnHalfCollapse = false)
        closeBtn.setOnClickListener { dismiss() }
        title.isSelected = true
        view?.findViewById<Button>(R.id.btnRepeat)?.setOnClickListener {
            presenter.input.accept(OnRetryLoadClicked)
        }
        actionButton.setOnClickListener { presenter.input.accept(OnGoToGroupClicked) }
    }

    private fun render(state: GroupDetailsState) {
        viewFlipper.displayedChild = when {
            state.fullScreenLoading -> FLIPPER_ITEM_LOADING
            state.fullScreenError -> FLIPPER_ITEM_ERROR
            else -> FLIPPER_ITEM_CONTENT
        }
        state.groupDetails?.let(::renderGroupDetails)
    }

    private fun handleEvent(event: GroupDetailsEvent) {
        when (event) {
            is OpenGroupDetails -> requireActivity().vkViewIntent(event.screenName)
        }
    }

    private fun renderGroupDetails(groupDetails: ExtendedGroupUi) {
        title.text = groupDetails.name
        subscribers.text = groupDetails.title
        if (groupDetails.description.isEmpty()) descriptionContainer.gone() else
            description.text = groupDetails.description
        wallpostDate.text = groupDetails.wallPostDate
    }

    companion object {
        private const val ARG_GROUP_ID = "ARG_GROUP_ID"

        fun newInstance(groupId: String): GroupDetailsBottomSheet {
            return GroupDetailsBottomSheet().apply {
                arguments = Bundle().apply {
                    putString(ARG_GROUP_ID, groupId)
                }
            }
        }
    }
}
