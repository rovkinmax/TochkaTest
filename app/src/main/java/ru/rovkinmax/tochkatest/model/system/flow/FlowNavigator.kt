package ru.rovkinmax.tochkatest.model.system.flow

import android.support.v4.app.Fragment
import ru.rovkinmax.tochkatest.R
import ru.terrakok.cicerone.commands.Command


abstract class FlowNavigator(fragment: Fragment,
                             private val router: GlobalRouter,
                             private val containerId: Int = R.id.container) : FragmentNavigator(fragment.activity!!.applicationContext, fragment.childFragmentManager, containerId) {

    private val fragmentManager = fragment.childFragmentManager

    override fun applyCommand(command: Command?) {
        when (command) {
            is PresetScreen -> preSetFragment(command)
            is ToggleScreen -> toggleFragment(command)
            else -> super.applyCommand(command)
        }
    }

    private fun preSetFragment(command: PresetScreen) {
        if (fragmentManager.findFragmentByTag(command.screenKey) == null) {
            val fragment = createFragment(command.screenKey, command.transitionData)
            if (fragment == null) {
                unknownScreen(command)
            } else {
                fragmentManager.beginTransaction()
                        .add(containerId, fragment, command.screenKey)
                        .hide(fragment)
                        .commitNow()
            }
        }
    }

    private fun toggleFragment(toggleScreen: ToggleScreen) {
        val newFragment = fragmentManager.findFragmentByTag(toggleScreen.newScreenKey)
        val oldFragment = fragmentManager.findFragmentByTag(toggleScreen.oldScreenKey)

        val transaction = fragmentManager.beginTransaction()
        newFragment?.let { transaction.show(it) }
        oldFragment?.let { transaction.hide(it) }
        transaction.commit()
    }

    override fun exit() {
        router.back()
    }

    override fun createFragment(screenKey: String?, data: Any?): Fragment? = null
}