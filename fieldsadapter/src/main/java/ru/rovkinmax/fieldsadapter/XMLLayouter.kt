package ru.rovkinmax.fieldsadapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ru.rovkinmax.fieldsadapter.matcher.FieldLayouter
import ru.rovkinmax.fieldsadapter.matcher.FieldViewHolder

typealias Matcher = ((Any) -> Boolean)
typealias Creator = ((View, ViewGroup) -> FieldViewHolder<*>)

class XMLLayouter(private var layoutId: Int) : FieldLayouter {

    private lateinit var mMatcher: Matcher
    private lateinit var mCreator: Creator


    fun matcher(matcher: Matcher) {
        mMatcher = matcher
    }

    fun creator(creator: Creator) {
        mCreator = { v, root -> creator(v, root) }
    }

    override fun isMatch(data: Any): Boolean {
        return mMatcher(data)
    }

    override fun onCreateViewHolder(root: ViewGroup): FieldViewHolder<*> {
        val res = LayoutInflater.from(root.context).inflate(layoutId, root, false)
        return mCreator(res, root)
    }
}

fun xmlLayouter(layoutId: Int, func: XMLLayouter.() -> (Unit)): XMLLayouter {
    val laouter = XMLLayouter(layoutId)
    laouter.func()
    return laouter
}