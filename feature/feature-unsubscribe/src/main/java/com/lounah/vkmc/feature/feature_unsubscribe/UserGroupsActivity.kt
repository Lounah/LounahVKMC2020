package com.lounah.vkmc.feature.feature_unsubscribe

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.lounah.vkmc.core.core_vk.domain.GetUserGroups
import com.lounah.vkmc.core.extensions.dp
import com.lounah.vkmc.core.recycler.Recycler
import com.lounah.vkmc.core.recycler.base.ViewTyped
import com.lounah.vkmc.feature.feature_unsubscribe.viewholders.GroupsHeaderUi
import com.lounah.vkmc.feature.feature_unsubscribe.viewholders.UserGroupUi
import com.lounah.vkmc.feature.feature_unsubscribe.viewholders.UserGroupsHolderFactory
import com.vk.api.sdk.auth.VKScope
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_user_groups.*
import kotlin.LazyThreadSafetyMode.NONE

class UserGroupsActivity : AppCompatActivity() {

    private val recycler: Recycler<ViewTyped> by lazy(NONE) {
        Recycler<ViewTyped>(userGroups, UserGroupsHolderFactory()) {
            itemDecoration = listOf(GridSpacesDecoration(12.dp(this@UserGroupsActivity)))
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        overridePendingTransition(0, 0)
        window.setFlags(FLAG_LAYOUT_NO_LIMITS, FLAG_LAYOUT_NO_LIMITS)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_groups)
        initRecycler()

        GetUserGroups()(0, 50)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { success, error ->
                val items = success.map { UserGroupUi(it.id.toString(), it.photo, it.name) }
                recycler.setItems(listOf(GroupsHeaderUi) + items)
            }
    }

    private fun initRecycler() {
        val layoutManager = GridLayoutManager(this, 3, RecyclerView.VERTICAL, false).apply {
            spanSizeLookup = UserGroupsSpanSizeLookup()
        }
        userGroups.layoutManager = layoutManager
    }

    companion object {
        val authScopes = listOf(VKScope.WALL, VKScope.PAGES, VKScope.GROUPS, VKScope.FRIENDS)

        fun start(context: Context) {
            Intent(context, UserGroupsActivity::class.java).also(context::startActivity)
        }
    }
}