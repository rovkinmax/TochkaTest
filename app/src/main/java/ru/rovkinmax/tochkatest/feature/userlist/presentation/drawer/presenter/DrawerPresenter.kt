package ru.rovkinmax.tochkatest.feature.userlist.presentation.drawer.presenter

import ru.rovkinmax.tochkatest.feature.global.domain.UserInfoEntity
import ru.rovkinmax.tochkatest.feature.global.presentation.ErrorHandler
import ru.rovkinmax.tochkatest.feature.global.presentation.presenter.Presenter
import ru.rovkinmax.tochkatest.feature.userlist.domain.UserInteractor
import ru.rovkinmax.tochkatest.feature.userlist.presentation.drawer.view.DrawerView
import ru.rovkinmax.tochkatest.model.system.flow.FlowRouter
import ru.rovkinmax.tochkatest.model.system.rx.async
import ru.rovkinmax.tochkatest.model.system.rx.subscribe
import javax.inject.Inject

class DrawerPresenter @Inject constructor(
        private val interactor: UserInteractor,
        private val errorHandler: ErrorHandler,
        router: FlowRouter) : Presenter<DrawerView>(router) {

    override fun onAttachView(v: DrawerView) {
        super.onAttachView(v)
        interactor.userInfo()
                .async()
                .compose(lifecycle())
                .subscribe(this::dispatchUserInfo, errorHandler.proceed(view))
    }

    private fun dispatchUserInfo(userInfo: UserInfoEntity) {
        view?.showName(userInfo.username)
        view?.showAvatar(userInfo.avatar)
    }
}