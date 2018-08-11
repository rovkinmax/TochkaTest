package ru.rovkinmax.tochkatest.model.ui.extentions

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager

inline fun <reified T : Fragment> FragmentManager.showFragment(fragment: T, tag: String = T::class.java.name) {
    beginTransaction()
            .add(fragment, tag)
            .commitAllowingStateLoss()
}
