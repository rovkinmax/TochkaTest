package ru.rovkinmax.tochkatest.model.ui.pagination

import android.support.v7.widget.RecyclerView
import io.reactivex.Observable
import io.reactivex.ObservableEmitter
import io.reactivex.ObservableOnSubscribe
import io.reactivex.android.MainThreadDisposable
import io.reactivex.android.schedulers.AndroidSchedulers


fun RecyclerView.paginationObservable(limit: Int): Observable<Int> {
    return getPaginationObservable(this, limit)
}

private fun getPaginationObservable(recyclerView: RecyclerView, limit: Int): Observable<Int> {
    checkAssertion(recyclerView)
    val onSubscribe = RecyclerViewScrollOnSubscribe(recyclerView, limit)
    return Observable.create(onSubscribe)
            .map { it.offset }
            .subscribeOn(AndroidSchedulers.mainThread())
            .distinctUntilChanged()
}

class ScrollChangeEvent(val offset: Int)

private class RecyclerViewScrollOnSubscribe(val recyclerView: RecyclerView, val limit: Int) : ObservableOnSubscribe<ScrollChangeEvent> {

    override fun subscribe(emitter: ObservableEmitter<ScrollChangeEvent>) {
        val lmHelper = LMHelper.getInstance(recyclerView.layoutManager)
        val sl = PaginationScrollListener(emitter, lmHelper, limit)
        recyclerView.addOnScrollListener(sl)
        emitter.setDisposable(mainThreadDisposable { recyclerView.removeOnScrollListener(sl) })
    }
}

private class PaginationScrollListener(private val emitter: ObservableEmitter<ScrollChangeEvent>,
                                       private val lmHelper: LMHelper<*>,
                                       private val limit: Int) : RecyclerView.OnScrollListener() {
    private var lastOffset = 0

    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        if (emitter.isDisposed.not()) {
            val position = lmHelper.lastVisibleItemPosition
            val count = recyclerView.adapter?.itemCount ?: 0
            val updatePosition = (count - 1) - limit / 2
            if (position >= updatePosition && count - lastOffset > 1) {
                lastOffset = count
                emitter.onNext(ScrollChangeEvent(lastOffset))
            }
        }
    }
}

private fun checkAssertion(recyclerView: RecyclerView) {
    if (recyclerView.layoutManager == null) {
        throw RuntimeException("layout manager is null")
    }
}

fun mainThreadDisposable(func: () -> Unit) = object : MainThreadDisposable() {
    override fun onDispose() = func()
}