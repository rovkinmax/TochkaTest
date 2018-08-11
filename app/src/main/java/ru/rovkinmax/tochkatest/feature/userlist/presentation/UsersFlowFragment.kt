package ru.rovkinmax.tochkatest.feature.userlist.presentation

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.view.View
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import ru.rovkinmax.socialnetwork.RxSocial
import ru.rovkinmax.tochkatest.R
import ru.rovkinmax.tochkatest.Screens
import ru.rovkinmax.tochkatest.di.*
import ru.rovkinmax.tochkatest.feature.global.FlowFragment
import ru.rovkinmax.tochkatest.feature.userlist.data.GithubApi
import ru.rovkinmax.tochkatest.feature.userlist.data.UserRepository
import ru.rovkinmax.tochkatest.feature.userlist.domain.UserInteractor
import ru.rovkinmax.tochkatest.feature.userlist.presentation.drawer.presenter.DrawerPresenter
import ru.rovkinmax.tochkatest.feature.userlist.presentation.search.presenter.UserSearchPresenter
import ru.rovkinmax.tochkatest.feature.userlist.presentation.search.view.UserSearchFragment
import ru.rovkinmax.tochkatest.model.system.flow.FlowNavigator
import ru.rovkinmax.tochkatest.model.system.flow.GlobalRouter
import toothpick.Toothpick

class UsersFlowFragment : FlowFragment() {

    companion object {
        fun newInstance(): UsersFlowFragment {
            return UsersFlowFragment().apply {
                arguments = Bundle()
            }
        }
    }

    override val layoutRes: Int = R.layout.fmt_flow_users

    override fun provideNavigator(router: GlobalRouter): FlowNavigator {
        return object : FlowNavigator(fragment = this, router = router) {

            override fun createFragment(screenKey: String?, data: Any?): Fragment? {
                return when (screenKey) {
                    Screens.SCREEN_USER_SEARCH -> UserSearchFragment.newInstance()
                    else -> null
                }
            }
        }
    }

    override fun injectDependencies() {
        Toothpick.openScopes(DI.SCOPE_APP, DI.SCOPE_FLOW_USERS).moduleFlow {
            bind(FragmentManager::class.java).toInstance(activity?.supportFragmentManager)
            bind(RxSocial::class.java).toProvider(RxSocialProvider::class.java).providesSingletonInScope()
            bind(OkHttpClient::class.java).toProvider(OkHttpProvider::class.java).providesSingletonInScope()
            bind(Retrofit::class.java).toProvider(RetrofitProvider::class.java).providesSingletonInScope()
            bind(GithubApi::class.java).toProvider(GithubApiProvider::class.java).providesSingletonInScope()

            bind(UserRepository::class.java).singletonInScope()
            bind(UserInteractor::class.java).singletonInScope()
            bind(DrawerPresenter::class.java)
            bind(UserSearchPresenter::class.java)
        }.inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navigator.setLaunchScreen(Screens.SCREEN_USER_SEARCH)
    }
}