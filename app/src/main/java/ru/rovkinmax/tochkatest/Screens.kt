package ru.rovkinmax.tochkatest

import ru.rovkinmax.tochkatest.di.DI
import ru.rovkinmax.tochkatest.feature.auth.presentation.view.AuthFlowFragment
import ru.rovkinmax.tochkatest.feature.global.FlowFragment
import ru.rovkinmax.tochkatest.feature.splash.presentation.view.SplashFragment
import ru.rovkinmax.tochkatest.feature.userlist.presentation.UsersFlowFragment
import toothpick.Toothpick

object Screens {

    const val FLOW_SPLASH = "FLOW_SPLASH"
    const val FLOW_AUTH = "FLOW_AUTH"
    const val SCREEN_LOGIN = "SCREEN_LOGIN"

    const val FLOW_USER_LIST = "FLOW_USER_LIST"
    const val SCREEN_USER_SEARCH = "SCREEN_USER_SEARCH"


    @Suppress("UNCHECKED_CAST")
    fun getFlowFragment(flowKey: String, data: Any? = null): FlowFragment? {
        return when (flowKey) {
            FLOW_SPLASH -> SplashFragment.newInstance()
            FLOW_AUTH -> {
                Toothpick.closeScope(DI.SCOPE_FLOW_USERS)
                AuthFlowFragment.newInstance()
            }

            FLOW_USER_LIST -> UsersFlowFragment.newInstance()
            else -> null
        }
    }
}