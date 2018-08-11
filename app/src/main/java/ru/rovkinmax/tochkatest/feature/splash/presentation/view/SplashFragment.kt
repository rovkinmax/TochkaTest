package ru.rovkinmax.tochkatest.feature.splash.presentation.view

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.View
import ru.rovkinmax.tochkatest.R
import ru.rovkinmax.tochkatest.di.DI
import ru.rovkinmax.tochkatest.di.moduleFlow
import ru.rovkinmax.tochkatest.feature.global.FlowFragment
import ru.rovkinmax.tochkatest.feature.global.presentation.presenter.Presenter
import ru.rovkinmax.tochkatest.feature.global.presentation.view.BaseView
import ru.rovkinmax.tochkatest.feature.splash.presentation.presenter.SplashPresenter
import ru.rovkinmax.tochkatest.model.system.flow.FlowNavigator
import ru.rovkinmax.tochkatest.model.system.flow.GlobalRouter
import toothpick.Toothpick

class SplashFragment : FlowFragment(), SplashView {

    companion object {
        private const val KEY_CHAIN = "chain"

        fun newInstance(): SplashFragment {
            return SplashFragment().apply {
                arguments = Bundle()
            }
        }
    }

    override val layoutRes: Int = R.layout.fmt_splash

    private val presenter by lazy {
        Toothpick.openScopes(DI.SCOPE_APP, scopeName).moduleFlow {
            bind(SplashPresenter::class.java)
        }.getInstance(SplashPresenter::class.java).also {
            Toothpick.closeScope(scopeName)
        }
    }

    @Suppress("UNCHECKED_CAST")
    override fun providePresenter(): Presenter<in BaseView>? = presenter as Presenter<in BaseView>?

    override fun provideNavigator(router: GlobalRouter): FlowNavigator = object : FlowNavigator(this, router) {
        override fun createFragment(screenKey: String?, data: Any?): Fragment? = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        presenter.loadConfig()
    }
}