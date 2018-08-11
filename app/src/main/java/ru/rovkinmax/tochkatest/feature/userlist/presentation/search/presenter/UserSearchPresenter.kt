package ru.rovkinmax.tochkatest.feature.userlist.presentation.search.presenter

import ru.rovkinmax.tochkatest.feature.global.presentation.ErrorHandler
import ru.rovkinmax.tochkatest.feature.global.presentation.RxDecor
import ru.rovkinmax.tochkatest.feature.global.presentation.errorView
import ru.rovkinmax.tochkatest.feature.global.presentation.loadingView
import ru.rovkinmax.tochkatest.feature.global.presentation.presenter.Presenter
import ru.rovkinmax.tochkatest.feature.userlist.domain.GithubUserEntity
import ru.rovkinmax.tochkatest.feature.userlist.domain.UserInteractor
import ru.rovkinmax.tochkatest.feature.userlist.presentation.search.view.UserSearchView
import ru.rovkinmax.tochkatest.model.system.flow.FlowRouter
import ru.rovkinmax.tochkatest.model.system.rx.async
import ru.rovkinmax.tochkatest.model.system.rx.subscribe
import javax.inject.Inject

class UserSearchPresenter @Inject constructor(private val router: FlowRouter,
                                              private val interactor: UserInteractor,
                                              private val errorHandler: ErrorHandler) : Presenter<UserSearchView>(router) {
    companion object {
        const val SIZE_PAGE = 30
    }

    private val mainProgressView = loadingView({ view?.showLoadingIndicator() }, { view?.hideLoadingIndicator() })
    private val pageProgressView = loadingView({ view?.showPaginationLoading() }, { view?.hidePaginationLoading() })
    private val pageErrorVIew = errorView({ view?.showPageError(it) }, { view?.hidePageError() })
    private var lastQuery: String? = null

    fun logout() {
        interactor.logout()
                .async()
                .compose(lifecycle<Any>())
                .subscribe({ router.navigateToAuth() }, errorHandler.proceed(view))
    }

    fun onSearchInputChanged(query: String) {
        if (query.isEmpty()) return
        lastQuery = query
        interactor.searchUser(query, SIZE_PAGE, 0)
                .async()
                .compose(lifecycle())
                .compose(RxDecor.loading(mainProgressView))
                .subscribe(this::dispatchSearchResult, errorHandler.proceed(view))
    }

    private fun dispatchSearchResult(list: List<GithubUserEntity>) {
        if (list.isEmpty())
            view?.showEmptyStub()
        else {
            view?.hideEmptyStub()
            view?.showUserList(list)
        }
    }

    fun loadNewPageWithOffset(realItemCount: Int) {
        view?.hidePageError()
        if (lastQuery != null) {
            interactor.searchUser(lastQuery.orEmpty(), SIZE_PAGE, realItemCount)
                    .async()
                    .compose(lifecycle())
                    .compose(RxDecor.loading(pageProgressView))
                    .subscribe({ view?.showNewPageUsers(it) }, errorHandler.proceed(pageErrorVIew))

        }
    }

    fun onItemClick(user: GithubUserEntity) {
        router.showSystemMessage(user.login)
    }
}