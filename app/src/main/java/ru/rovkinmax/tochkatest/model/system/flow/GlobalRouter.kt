package ru.rovkinmax.tochkatest.model.system.flow

import ru.rovkinmax.tochkatest.Screens
import ru.terrakok.cicerone.Router
import ru.terrakok.cicerone.commands.Back

open class GlobalRouter : Router() {

    fun sendResult(resultCode: Int): Boolean {
        return super.sendResult(resultCode, null)
    }

    public override fun sendResult(resultCode: Int, result: Any?): Boolean {
        return super.sendResult(resultCode, result)
    }

    fun backWithResult(resultCode: Int, result: Any?) {
        back()
        sendResult(resultCode, result)
    }

    fun back() {
        executeCommands(Back())
    }

    open fun navigateToAuth() {
        newRootScreen(Screens.FLOW_AUTH)
    }
}