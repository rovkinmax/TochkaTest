package ru.rovkinmax.tochkatest.feature.userlist.presentation.drawer.view

import ru.rovkinmax.tochkatest.feature.global.presentation.view.BaseView

interface DrawerView : BaseView {
    fun showAvatar(avatar: String)
    fun showName(username: String)
}