package ru.rovkinmax.tochkatest.feature.splash.presentation.presenter

import ru.rovkinmax.tochkatest.Screens
import ru.rovkinmax.tochkatest.feature.global.presentation.RxError
import ru.rovkinmax.tochkatest.feature.global.presentation.presenter.Presenter
import ru.rovkinmax.tochkatest.feature.splash.domain.SplashInteractor
import ru.rovkinmax.tochkatest.feature.splash.presentation.view.SplashView
import ru.rovkinmax.tochkatest.model.system.flow.FlowRouter
import ru.rovkinmax.tochkatest.model.system.rx.async
import ru.rovkinmax.tochkatest.model.system.rx.subscribe
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class SplashPresenter @Inject constructor(private val interactor: SplashInteractor,
                                          private val router: FlowRouter) : Presenter<SplashView>(router) {

    companion object {

        private const val TIME_LOADING = 1000L
    }

    fun loadConfig() {
        interactor.isAlreadyLogin()
                .delay(TIME_LOADING, TimeUnit.MILLISECONDS)
                .async()
                .compose(lifecycle())
                .subscribe(this::dispatchResult, RxError.doNothing())
    }

    private fun dispatchResult(isAlreadyLogin: Boolean) {
        if (isAlreadyLogin)
            router.startNewRootFlow(Screens.FLOW_USER_LIST)
        else router.startNewRootFlow(Screens.FLOW_AUTH)
    }

    override fun onBackPressed() {}
}