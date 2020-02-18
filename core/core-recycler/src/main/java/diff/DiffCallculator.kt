package com.lounah.vkmc.core.recycler.diff

import androidx.recyclerview.widget.DiffUtil
import com.lounah.vkmc.core.recycler.Adapter
import com.lounah.vkmc.core.recycler.base.ViewTyped
import com.lounah.vkmc.core.recycler.base.items.ProgressItem

interface DiffResult<T : ViewTyped> {
    val items: List<T>
    fun update(adapter: Adapter<T>)

    class LoadingDiffResult(viewTyped: ViewTyped? = ProgressItem) : DiffResult<ViewTyped> {
        override val items: List<ViewTyped> = listOf(viewTyped ?: ProgressItem)
        override fun update(adapter: Adapter<ViewTyped>) {
            adapter.notifyDataSetChanged()
        }
    }

    object EmptyDiffResult :
        DiffResult<ViewTyped> {
        override val items: List<ViewTyped> = emptyList()
        override fun update(adapter: Adapter<ViewTyped>) {
            adapter.notifyDataSetChanged()
        }
    }
}

private class DiffResultImpl<T : ViewTyped>(
    private val diffResult: DiffUtil.DiffResult,
    override val items: List<T>
) :
    DiffResult<T> {

    override fun update(adapter: Adapter<T>) {
        diffResult.dispatchUpdatesTo(adapter)
    }
}

interface DiffCalculator<T : ViewTyped> : (List<T>, List<T>) -> DiffResult<T>

class DiffCalculatorImpl<T : ViewTyped>(private val diffCallbackExtender: (oldList: List<T>, newList: List<T>) -> DiffUtil.Callback) :
    DiffCalculator<T> {

    override fun invoke(oldList: List<T>, newList: List<T>): DiffResult<T> {
        val diffResult = DiffUtil.calculateDiff(diffCallbackExtender(oldList, newList))
        return DiffResultImpl(diffResult, newList)
    }
}

val DefaultDiffCalculator =
    DiffCalculatorImpl { oldList: List<ViewTyped>, newList: List<ViewTyped> ->
        object : DiffUtil.Callback() {
            override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                return oldList[oldItemPosition].uid == newList[newItemPosition].uid
            }

            override fun getOldListSize(): Int = oldList.size

            override fun getNewListSize(): Int = newList.size

            override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                return oldList[oldItemPosition] == newList[newItemPosition]
            }
        }
    }