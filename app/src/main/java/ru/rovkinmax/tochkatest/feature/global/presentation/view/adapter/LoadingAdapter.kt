package ru.rovkinmax.tochkatest.feature.global.presentation.view.adapter

import android.os.Handler
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.error_view.view.*
import ru.rovkinmax.fieldsadapter.FieldsAdapter
import ru.rovkinmax.fieldsadapter.matcher.FieldViewHolder
import ru.rovkinmax.tochkatest.R

abstract class LoadingAdapter<T : Any> : FieldsAdapter<Any>() {

    companion object {
        private val LOADING_ITEM = Item.LoadingItem
    }

    private val handler = Handler()

    init {
        addXmlLayouter(R.layout.item_loading) {
            matcher { it is Item.LoadingItem }
            creator { view, viewGroup -> LoadingViewHolder(view, viewGroup) }
        }

        addXmlLayouter(R.layout.error_view) {
            matcher { it is Item.ErrorItem }
            creator { view, viewGroup -> ErrorViewHolder(view, viewGroup) }
        }
    }


    private val items = arrayListOf<T>()

    open fun getRealItemCount(): Int = items.size

    open fun addDataSet(list: List<T>) {
        val oldCount = items.size
        items.addAll(list)
        setList(items)
        handler.post {
            notifyItemRemoved(oldCount)
            notifyItemRangeInserted(oldCount, list.size)
        }
    }

    open fun changeDataSet(itemList: List<T>) {
        val oldCount = items.size
        val newSize = itemList.size
        items.clear()
        items.addAll(itemList)
        setList(items)
        handler.post {
            if (oldCount > newSize) notifyItemRangeRemoved(newSize, oldCount - newSize)
            notifyItemRangeChanged(0, newSize)
        }
    }

    fun showLoading() {
        val list = getList().toMutableList()
        if (!list.contains(LOADING_ITEM)) {
            val oldCount = list.size
            list.add(LOADING_ITEM)
            setList(list)
            handler.post { notifyItemInserted(oldCount + 1) }
        }
    }

    fun hideLoading() {
        val list = getList().toMutableList()
        val oldCount = list.size
        if (list.remove(LOADING_ITEM))
            handler.post { notifyItemRemoved(oldCount) }
    }

    fun showError(message: String, action: (() -> Unit)) {
        val itemList = getList().toMutableList()
        if (itemList.filterIsInstance(Item.ErrorItem::class.java).isEmpty()) {
            itemList.remove(LOADING_ITEM)
            itemList.add(Item.ErrorItem(message, action))
            setList(itemList)
            handler.post { notifyItemChanged(itemList.size) }
        }
    }

    fun hideError() {
        val itemList = getList().toMutableList()
        val index = itemList.indexOfFirst { it is Item.ErrorItem }
        if (index >= 0) {
            itemList.removeAt(index)
            setList(itemList)
            handler.post { notifyItemRemoved(index) }
        }
    }

    open fun removeItem(item: T) {
        val index = items.indexOf(item)
        if (index >= 0) {
            items.removeAt(index)
            setList(items)
            notifyItemRemoved(index)
        }
    }

    protected fun removeItems(vararg arrayOfItem: T) {
        if (arrayOfItem.isNotEmpty()) {
            val index = items.indexOf(arrayOfItem.component1())
            items.removeAll(arrayOfItem)
            setList(items)
            notifyItemRangeRemoved(index, arrayOfItem.size)
        }
    }

    private sealed class Item {
        object LoadingItem : Item()
        class ErrorItem(val message: String, val action: (() -> Unit)? = null) : Item()
    }

    private class LoadingViewHolder(itemView: View, root: ViewGroup) : FieldViewHolder<Item.LoadingItem>(itemView, root)
    private class ErrorViewHolder(itemView: View, root: ViewGroup) : FieldViewHolder<Item.ErrorItem>(itemView, root) {

        override fun performBind(data: Item.ErrorItem) {
            itemView.tvError.text = data.message
            itemView.btnRetry.setOnClickListener { data.action?.invoke() }
        }
    }
}