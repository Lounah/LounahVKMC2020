package com.lounah.vkmc.feature.feature_unsubscribe.usergroups.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.lounah.vkmc.core.di.ComponentStorage.getComponent
import com.lounah.vkmc.core.extensions.asType
import com.lounah.vkmc.core.extensions.disposeOnDestroy
import com.lounah.vkmc.core.extensions.dp
import com.lounah.vkmc.core.extensions.subscribeTo
import com.lounah.vkmc.feature.feature_unsubscribe.R
import com.lounah.vkmc.feature.feature_unsubscribe.di.UserGroupsComponent
import com.lounah.vkmc.feature.feature_unsubscribe.groupdetails.ui.GroupDetailsBottomSheet
import com.lounah.vkmc.feature.feature_unsubscribe.usergroups.presentation.UserGroupsAction.*
import com.lounah.vkmc.feature.feature_unsubscribe.usergroups.presentation.UserGroupsEvent
import com.lounah.vkmc.feature.feature_unsubscribe.usergroups.presentation.UserGroupsEvent.*
import com.lounah.vkmc.feature.feature_unsubscribe.usergroups.presentation.UserGroupsPresenter
import com.lounah.vkmc.feature.feature_unsubscribe.usergroups.presentation.UserGroupsState
import com.lounah.vkmc.feature.feature_unsubscribe.usergroups.ui.experimental.UserGroupsAdapter
import com.vk.api.sdk.auth.VKScope
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_user_groups.*
import java.util.concurrent.TimeUnit
import kotlin.LazyThreadSafetyMode.NONE

class UserGroupsActivity : AppCompatActivity() {

    private val presenter: UserGroupsPresenter by lazy(NONE) {
        getComponent<UserGroupsComponent>().presenter
    }

//    private val recycler: Recycler<ViewTyped> by lazy(NONE) {
//        Recycler<ViewTyped>(userGroups, UserGroupsHolderFactory()) {
//            itemDecoration = listOf(GridSpacesDecoration(12.dp(this@UserGroupsActivity)))
//            layoutManager = object :
//                GridLayoutManager(this@UserGroupsActivity, 3) {
////                override fun supportsPredictiveItemAnimations(): Boolean {
////                    return false
////                }
//            }
//                .apply {
//                    spanSizeLookup = UserGroupsSpanSizeLookup { recycler.adapter.items }
//                }
//        }
//    }

    private val adapter: UserGroupsAdapter by lazy(NONE) {
        UserGroupsAdapter({
            presenter.input.accept(OnGroupSelected(it.uid))
        }, {
            presenter.input.accept(OnGroupLongTapped(it.uid))
        }, {

        })
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        overridePendingTransition(0, 0)
        window.setFlags(FLAG_LAYOUT_NO_LIMITS, FLAG_LAYOUT_NO_LIMITS)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_groups)
        initBindings()
        userGroups.layoutManager = object :
            GridLayoutManager(this@UserGroupsActivity, 3, VERTICAL, false) {
//                override fun supportsPredictiveItemAnimations(): Boolean {
//                    return true
//                }
        }
            .apply {
                spanSizeLookup = UserGroupsSpanSizeLookup { adapter.items }
            }
        userGroups.addItemDecoration((GridSpacesDecoration(12.dp(this@UserGroupsActivity))))
        userGroups.adapter = adapter
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

//        UserGroupsRecyclerEvents(
//            onRepeatLoadingClicked = Observable.merge(
//                recycler.clickedViewId(R.layout.item_paging_error, R.id.btnRepeat),
//                recycler.clickedViewId(R.layout.item_error, R.id.btnRepeat)
//            ),
//            onGroupSelected = recycler.clickedItem(R.layout.item_user_group),
//            onGroupLongTap = recycler.longTappedItem(R.layout.item_user_group),
//            loadNextPage = userGroups.endlessScrollObservable(),
//            input = presenter.input
//        ).bind(this)
//        UserGroupsRecyclerEvents(
//            loadNextPage = userGroups.endlessScrollObservable(),
//            input = presenter.input
//        ).bind(this)

        presenter.input.accept(InitialLoading)
        val upd = Observable.interval(5, TimeUnit.SECONDS)
            .delay(1, TimeUnit.SECONDS)
            .map {
                presenter.input.accept(OnPageScrolled(it.toInt() * 2))
            }.subscribe()
    }

    private fun render(state: UserGroupsState) {
        adapter.setItems(state.userGroups.asType())
//        if (state.userGroups.isNotEmpty()) {
//            val m = state.userGroups.toMutableList()
//            Handler().postDelayed({
//                adapter.setItems(m.asReversed().asType())
//            }, 3000)
//        }

//        if (state.shouldUpdate) recycler.setItems(state.diff)
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

    companion object {
        val authScopes = listOf(VKScope.WALL, VKScope.PAGES, VKScope.GROUPS, VKScope.FRIENDS)

        fun start(context: Context) {
            Intent(context, UserGroupsActivity::class.java).also(context::startActivity)
        }
    }
}