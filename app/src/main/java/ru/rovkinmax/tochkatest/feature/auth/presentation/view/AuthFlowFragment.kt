package ru.rovkinmax.tochkatest.feature.auth.presentation.view

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.View
import ru.rovkinmax.socialnetwork.fb.RxFb
import ru.rovkinmax.socialnetwork.plus.RxPlus
import ru.rovkinmax.socialnetwork.vk.RxVk
import ru.rovkinmax.tochkatest.Screens
import ru.rovkinmax.tochkatest.di.DI
import ru.rovkinmax.tochkatest.di.inject
import ru.rovkinmax.tochkatest.di.moduleFlow
import ru.rovkinmax.tochkatest.feature.auth.data.LoginRepository
import ru.rovkinmax.tochkatest.feature.auth.domain.LoginInteractor
import ru.rovkinmax.tochkatest.feature.auth.presentation.presenter.LoginPresenter
import ru.rovkinmax.tochkatest.feature.global.FlowFragment
import ru.rovkinmax.tochkatest.model.system.flow.FlowNavigator
import ru.rovkinmax.tochkatest.model.system.flow.GlobalRouter
import toothpick.Toothpick

class AuthFlowFragment : FlowFragment() {

    companion object {
        fun newInstance(): AuthFlowFragment {
            return AuthFlowFragment().apply {
                arguments = Bundle()
            }
        }
    }

    override fun provideNavigator(router: GlobalRouter): FlowNavigator {
        return object : FlowNavigator(this, router) {
            override fun createFragment(screenKey: String?, data: Any?): Fragment? {
                return when (screenKey) {
                    Screens.SCREEN_LOGIN -> LoginFragment.newInstance()
                    else -> null
                }
            }
        }
    }

    override fun injectDependencies() {
        Toothpick.openScopes(DI.SCOPE_APP, DI.SCOPE_FLOW_AUTH).moduleFlow {
            bind(LoginRepository::class.java).singletonInScope()
            bind(LoginInteractor::class.java).singletonInScope()
            bind(RxVk::class.java).toInstance(RxVk(activity!!.supportFragmentManager))
            bind(RxFb::class.java).toInstance(RxFb(activity!!.supportFragmentManager))
            bind(RxPlus::class.java).toInstance(RxPlus(activity!!.supportFragmentManager))
            bind(LoginPresenter::class.java)
        }.inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navigator.setLaunchScreen(Screens.SCREEN_LOGIN)
    }
}