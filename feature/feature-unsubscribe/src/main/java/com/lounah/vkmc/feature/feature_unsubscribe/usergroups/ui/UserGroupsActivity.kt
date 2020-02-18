package com.lounah.vkmc.feature.feature_unsubscribe.usergroups.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.lounah.vkmc.core.di.ComponentStorage.getComponent
import com.lounah.vkmc.core.extensions.asType
import com.lounah.vkmc.core.extensions.disposeOnDestroy
import com.lounah.vkmc.core.extensions.dp
import com.lounah.vkmc.core.extensions.subscribeTo
import com.lounah.vkmc.core.recycler.paging.core.pagedScrollListener
import com.lounah.vkmc.feature.feature_unsubscribe.R
import com.lounah.vkmc.feature.feature_unsubscribe.di.UserGroupsComponent
import com.lounah.vkmc.feature.feature_unsubscribe.groupdetails.ui.GroupDetailsBottomSheet
import com.lounah.vkmc.feature.feature_unsubscribe.usergroups.presentation.UserGroupsAction.*
import com.lounah.vkmc.feature.feature_unsubscribe.usergroups.presentation.UserGroupsEvent
import com.lounah.vkmc.feature.feature_unsubscribe.usergroups.presentation.UserGroupsEvent.*
import com.lounah.vkmc.feature.feature_unsubscribe.usergroups.presentation.UserGroupsPresenter
import com.lounah.vkmc.feature.feature_unsubscribe.usergroups.presentation.UserGroupsState
import com.lounah.vkmc.feature.feature_unsubscribe.usergroups.ui.recycler.UserGroupUi
import com.lounah.vkmc.feature.feature_unsubscribe.usergroups.ui.recycler.UserGroupsAdapter
import com.vk.api.sdk.auth.VKScope
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_user_groups.*
import kotlin.LazyThreadSafetyMode.NONE

class UserGroupsActivity : AppCompatActivity() {

    private val presenter: UserGroupsPresenter by lazy(NONE) {
        getComponent<UserGroupsComponent>().presenter
    }

    private val adapter: UserGroupsAdapter by lazy(NONE) {
        UserGroupsAdapter(::onGroupClicked, ::onGroupLongClicked, ::onRepeatLoading)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        overridePendingTransition(0, 0)
        window.setFlags(FLAG_LAYOUT_NO_LIMITS, FLAG_LAYOUT_NO_LIMITS)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_groups)
        initBindings()
        initRecycler()
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

    private fun render(state: UserGroupsState) {
        adapter.setItems(state.userGroups)
    }

    private fun handleEvent(event: UserGroupsEvent) {
        when (event) {
            is OpenExtraGroupInfoDialog -> {
                GroupDetailsBottomSheet.newInstance(event.groupId)
                    .show(supportFragmentManager, null)
            }
            is ShowGroupsLeaveError -> {
            }
            is ShowGroupsLeaveSuccess -> {
            }
        }
    }

    private fun initRecycler() {
        userGroups.layoutManager = GridLayoutManager(this@UserGroupsActivity, 3, GridLayoutManager.VERTICAL, false)
            .apply {
                spanSizeLookup = UserGroupsSpanSizeLookup { adapter.items }
            }
        userGroups.addItemDecoration((GridSpacesDecoration(12.dp(this@UserGroupsActivity))))
        userGroups.adapter = adapter
        userGroups.pagedScrollListener { presenter.input.accept(OnPageScrolled(it)) }
    }

    private fun onGroupClicked(group: UserGroupUi) {
        presenter.input.accept(OnGroupSelected(group.uid))
    }

    private fun onGroupLongClicked(group: UserGroupUi) {
        presenter.input.accept(OnGroupLongTapped(group.uid))
    }

    private fun onRepeatLoading() {
        presenter.input.accept(OnRetryLoadingClicked)
    }

    companion object {
        val authScopes = listOf(VKScope.WALL, VKScope.PAGES, VKScope.GROUPS, VKScope.FRIENDS)

        fun start(context: Context) {
            Intent(context, UserGroupsActivity::class.java).also(context::startActivity)
        }
    }
}
