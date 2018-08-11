package ru.rovkinmax.tochkatest.model.system.flow

import ru.terrakok.cicerone.result.ResultListener


class FlowRouter constructor(private val appRouter: GlobalRouter) : GlobalRouter() {

    companion object {
        private const val REQUEST_CODE_INVALID = -1
    }

    fun startFlow(flowKey: String, data: Any? = null) {
        appRouter.navigateTo(flowKey, data)
    }

    fun replaseFlow(flowKey: String, data: Any? = null) {
        appRouter.replaceScreen(flowKey, data)
    }

    fun finishFlow(data: Any? = null, resultCode: Int = REQUEST_CODE_INVALID) {
        if (resultCode != REQUEST_CODE_INVALID) appRouter.exitWithResult(resultCode, data)
        else appRouter.exit()
    }

    fun startNewRootFlow(flowKey: String, data: Any? = null) {
        appRouter.newRootScreen(flowKey, data)
    }

    fun preSetScreens(vararg screenKeys: String) {
        preSetScreens(*screenKeys.map { Pair(it, null) }.toTypedArray())
    }

    fun preSetScreens(vararg pairs: Pair<String, Any?>) {
        executeCommands(*pairs.map { PresetScreen(it.first, it.second) }.toTypedArray())
    }

    fun toggleScreen(newScreenKey: String, oldScreenKey: String) {
        executeCommands(ToggleScreen(newScreenKey, oldScreenKey))
    }

    override fun navigateToAuth() {
        appRouter.navigateToAuth()
    }

    override fun setResultListener(resultCode: Int, listener: ResultListener?) {
        super.setResultListener(resultCode, listener)
        appRouter.setResultListener(resultCode, listener)
    }

    override fun removeResultListener(resultCode: Int) {
        super.removeResultListener(resultCode)
        appRouter.removeResultListener(resultCode)
    }

    fun sendGlobalResult(resultCode: Int, result: Any? = null) {
        appRouter.sendResult(resultCode, result)
    }
}