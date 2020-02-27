package com.lounah.vkmc.feature.feature_map.details.ui

import android.app.Dialog
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.Button
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.lounah.vkmc.core.core_vk.vkViewIntent
import com.lounah.vkmc.core.di.ComponentStorage.getComponent
import com.lounah.vkmc.core.extensions.disposeOnDestroy
import com.lounah.vkmc.core.extensions.gone
import com.lounah.vkmc.core.extensions.subscribeTo
import com.lounah.vkmc.core.ui.bottomsheet.BaseBottomSheetDialogFragment
import com.lounah.vkmc.core.ui.bottomsheet.attachStickyFooter
import com.lounah.vkmc.feature.feature_map.R
import com.lounah.vkmc.feature.feature_map.details.presentation.GroupOrEventAction.OnRetryLoadClicked
import com.lounah.vkmc.feature.feature_map.details.presentation.GroupOrEventDetailsPresenter
import com.lounah.vkmc.feature.feature_map.details.presentation.GroupOrEventState
import com.lounah.vkmc.feature.feature_map.di.EventsMapComponent
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_group_or_event_details.*


private const val FLIPPER_ITEM_LOADING = 0
private const val FLIPPER_ITEM_ERROR = 1
private const val FLIPPER_ITEM_CONTENT = 2

internal class GroupOrEventDetailsDialog :
BaseBottomSheetDialogFragment(R.layout.fragment_group_or_event_details, expandByDefault = false) {

    private val presenter: GroupOrEventDetailsPresenter by lazy(LazyThreadSafetyMode.NONE) {
        val groupId = arguments?.getString(ARG_GROUP_ID).orEmpty()
        val groupAddress = arguments?.getString(ARG_GROUP_ADDRESS).orEmpty()
        getComponent<EventsMapComponent>().groupOrEventPresenterFactory(groupId, groupAddress)
    }

    private lateinit var screenName: String

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return BottomSheetDialog(requireActivity(), R.style.BottomSheetDialogTheme_Light)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initBindings()
        initUi()
    }

    override fun onStart() {
        super.onStart()
        val windowParams = dialog?.window!!.attributes
        windowParams.dimAmount = 0f
        windowParams.flags = windowParams.flags or WindowManager.LayoutParams.FLAG_DIM_BEHIND
        dialog?.window!!.attributes = windowParams
    }

    private fun initBindings() {
        presenter.state
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeTo(onNext = ::render)
            .disposeOnDestroy(viewLifecycleOwner)
    }

    private fun initUi() {
        dialog?.attachStickyFooter(buttonContainer, dismissOnHalfCollapse = false)
        closeBtn.setOnClickListener { dismiss() }
        view?.findViewById<Button>(R.id.btnRepeat)?.setOnClickListener {
            presenter.input.accept(OnRetryLoadClicked)
        }
        actionButton.setOnClickListener { context?.vkViewIntent(screenName) }
    }

    private fun render(state: GroupOrEventState) {
        viewFlipper.displayedChild = when {
            state.fullScreenLoading -> FLIPPER_ITEM_LOADING
            state.fullScreenError -> FLIPPER_ITEM_ERROR
            else -> FLIPPER_ITEM_CONTENT
        }
        state.groupDetails?.let(::renderGroupDetails)
    }

    private fun renderGroupDetails(groupDetails: GroupOrEventUi) {
        title.text = groupDetails.title
        location.text = groupDetails.address
        screenName = groupDetails.screenName
        if (groupDetails.description.isEmpty()) descriptionContainer.gone() else
            description.text = groupDetails.description
    }

    companion object {
        private const val ARG_GROUP_ID = "group_id"
        private const val ARG_GROUP_ADDRESS = "group_address"

        fun newInstance(groupId: String, groupAddress: String): GroupOrEventDetailsDialog {
            return GroupOrEventDetailsDialog().apply {
                arguments = Bundle().apply {
                    putString(ARG_GROUP_ID, groupId)
                    putString(ARG_GROUP_ADDRESS, groupAddress)
                }
            }
        }
    }
}
