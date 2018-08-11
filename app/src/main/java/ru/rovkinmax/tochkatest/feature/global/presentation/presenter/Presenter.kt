package ru.rovkinmax.tochkatest.feature.global.presentation.presenter

import android.support.annotation.CallSuper
import ru.rovkinmax.tochkatest.feature.global.presentation.view.BaseView
import ru.rovkinmax.tochkatest.model.system.flow.FlowRouter
import ru.rovkinmax.tochkatest.model.ui.rx.LifecycleProvider
import ru.rovkinmax.tochkatest.model.ui.rx.LifecycleTransformer

abstract class Presenter<V : BaseView?>(private val router: FlowRouter) {

    protected var view: V? = null
    private val provider = LifecycleProvider()
    protected fun <T> lifecycle(): LifecycleTransformer<T, T> = provider.lifecycle()

    @CallSuper
    open fun onAttachView(v: V) {
        view = v
    }

    @CallSuper
    open fun onDetachView(v: V) {
        view = null
        provider.unsubscribe()
    }

    open fun onBackPressed() {
        router.back()
    }
}