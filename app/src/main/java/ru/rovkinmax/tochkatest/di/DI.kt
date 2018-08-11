package ru.rovkinmax.tochkatest.di

import android.content.Context
import toothpick.Toothpick

object DI {
    const val SCOPE_APP = "SCOPE_APP"
    const val SCOPE_FLOW_AUTH = "SCOPE_FLOW_AUTH"
    const val SCOPE_FLOW_USERS = "SCOPE_FLOW_USERS"

    fun initAppScope(context: Context) {
        Toothpick.openScope(SCOPE_APP).apply {
            installModules(moduleApp(context))
        }
    }
}