package ru.rovkinmax.tochkatest.feature.userlist.presentation.search.view

import ru.rovkinmax.tochkatest.feature.global.presentation.view.BaseView
import ru.rovkinmax.tochkatest.feature.global.presentation.view.EmptyView
import ru.rovkinmax.tochkatest.feature.global.presentation.view.PaginationView
import ru.rovkinmax.tochkatest.feature.userlist.domain.GithubUserEntity

interface UserSearchView : BaseView, PaginationView, EmptyView {
    fun showUserList(userList: List<GithubUserEntity>)
    fun showPageError(message: String)
    fun hidePageError()
    fun showNewPageUsers(userList: List<GithubUserEntity>)
}