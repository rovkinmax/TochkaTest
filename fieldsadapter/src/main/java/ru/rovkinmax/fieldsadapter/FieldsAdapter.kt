package ru.rovkinmax.fieldsadapter

import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import ru.rovkinmax.fieldsadapter.matcher.FieldViewHolder
import ru.rovkinmax.fieldsadapter.matcher.ViewHolderMatcher
import java.util.*

open class FieldsAdapter<T : Any> : RecyclerView.Adapter<FieldViewHolder<T>>() {

    private var objectList: List<T> = ArrayList()

    protected val matcher: ViewHolderMatcher<T> = ViewHolderMatcher()

    override fun getItemViewType(position: Int): Int {
        return matcher.getMatchViewType(objectList[position])
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FieldViewHolder<T> {
        return matcher.onCreateViewHolder(parent, viewType) ?: throw IllegalArgumentException()
    }

    override fun onBindViewHolder(holder: FieldViewHolder<T>, position: Int) {
        val item = objectList[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int = objectList.size

    fun setList(list: List<T>) {
        this.objectList = list
    }

    fun getList(): List<T> = objectList

    fun addXmlLayouter(resId: Int, func: XMLLayouter.() -> (Unit)) {
        matcher.add(xmlLayouter(resId, func))
    }
}
