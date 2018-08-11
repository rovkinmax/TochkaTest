package ru.rovkinmax.fieldsadapter.matcher

import android.view.ViewGroup

interface FieldLayouter {
    fun isMatch(data: Any): Boolean

    fun onCreateViewHolder(root: ViewGroup): FieldViewHolder<*>
}
