package ru.rovkinmax.tochkatest.model.system.flow

import android.content.Context
import android.support.v4.app.FragmentManager
import android.widget.Toast
import ru.terrakok.cicerone.android.SupportFragmentNavigator
import ru.terrakok.cicerone.commands.BackTo
import ru.terrakok.cicerone.commands.Replace

abstract class FragmentNavigator(private val context: Context,
                                 fragmentManager: FragmentManager,
                                 containerId: Int) : SupportFragmentNavigator(fragmentManager, containerId) {

    fun setLaunchScreen(screenKey: String, data: Any? = null) {
        applyCommands(arrayOf(
                BackTo(null),
                Replace(screenKey, data)
        ))
    }

    override fun showSystemMessage(message: String?) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }
}