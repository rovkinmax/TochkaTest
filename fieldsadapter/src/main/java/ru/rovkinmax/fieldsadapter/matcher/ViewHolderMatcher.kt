package ru.rovkinmax.fieldsadapter.matcher

import android.view.ViewGroup
import java.util.*

class ViewHolderMatcher<T : Any> {

    private val layouters = ArrayList<FieldLayouter>()
    private val layoutersViewTypes: MutableMap<FieldLayouter, Int> = HashMap()
    private var incrementViewType = 0

    fun add(layouter: FieldLayouter): ViewHolderMatcher<*> {
        storeViewType(layouter)
        if (layouters.contains(layouter).not()) {
            layouters.add(layouter)
        }
        return this
    }

    private fun storeViewType(layouter: FieldLayouter) {
        if (layoutersViewTypes.keys.contains(layouter).not()) {
            layoutersViewTypes[layouter] = incrementViewType
            incrementViewType++
        }
    }

    fun getMatchViewType(content: T): Int {
        val layouter = layouters.firstOrNull { it.isMatch(content) }
        val viewType = layouter?.let { getLayouterId(it) } ?: -1
        return viewType
    }


    private fun getLayouterId(layouter: FieldLayouter): Int {
        val res = layoutersViewTypes[layouter]
        return res ?: -1
    }

    @Suppress("UNCHECKED_CAST")
    fun onCreateViewHolder(root: ViewGroup, viewType: Int): FieldViewHolder<T>? {
        val matchedLayouter: FieldLayouter? = layouters.firstOrNull { layouter -> viewType == layoutersViewTypes[layouter] }
        return matchedLayouter?.onCreateViewHolder(root) as FieldViewHolder<T>?
    }
}