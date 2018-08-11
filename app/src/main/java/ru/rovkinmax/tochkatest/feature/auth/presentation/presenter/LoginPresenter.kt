package ru.rovkinmax.tochkatest.feature.auth.presentation.presenter

import io.reactivex.Observable
import ru.rovkinmax.socialnetwork.SocialLoginResult
import ru.rovkinmax.tochkatest.Screens
import ru.rovkinmax.tochkatest.di.DI
import ru.rovkinmax.tochkatest.feature.auth.domain.LoginInteractor
import ru.rovkinmax.tochkatest.feature.auth.presentation.view.LoginView
import ru.rovkinmax.tochkatest.feature.global.presentation.ErrorHandler
import ru.rovkinmax.tochkatest.feature.global.presentation.presenter.Presenter
import ru.rovkinmax.tochkatest.model.system.flow.FlowRouter
import ru.rovkinmax.tochkatest.model.system.rx.async
import ru.rovkinmax.tochkatest.model.system.rx.subscribe
import toothpick.Toothpick
import javax.inject.Inject

class LoginPresenter @Inject constructor(
        private val loginInteractor: LoginInteractor,
        private val router: FlowRouter,
        private val errorHandler: ErrorHandler) : Presenter<LoginView>(router) {

    fun onLoginVkClicked() {
        loginWithSocial(loginInteractor.loginVk())
    }

    fun onLoginFbClicked() {
        loginWithSocial(loginInteractor.loginFb())
    }

    fun onLoginPlusClicked() {
        loginWithSocial(loginInteractor.loginPlus())
    }

    private fun loginWithSocial(loginCompletable: Observable<SocialLoginResult>) {
        loginCompletable
                .async()
                .subscribe(this::dispatchLoginResult, errorHandler.proceed(view))
    }

    private fun dispatchLoginResult(loginResult: SocialLoginResult) {
        Toothpick.closeScope(DI.SCOPE_FLOW_AUTH)
        router.startNewRootFlow(Screens.FLOW_USER_LIST)
    }
}