package ru.rovkinmax.tochkatest.feature.global.presentation


import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import io.reactivex.functions.Consumer
import ru.rovkinmax.tochkatest.feature.global.presentation.view.ErrorView
import ru.rovkinmax.tochkatest.feature.global.presentation.view.dialog.ErrorMessageDialog
import timber.log.Timber

object RxError {

    fun view(fragment: Fragment): ErrorView {
        return view(fragment.fragmentManager!!)
    }

    fun view(fm: FragmentManager, func: (() -> Unit)? = null): ErrorView {
        return object : ErrorView {
            override fun showErrorMessage(message: String, needCallback: Boolean) {
                ErrorMessageDialog.show(
                        fragmentManager = fm,
                        message = message,
                        func = if (needCallback) func else null)
            }
        }
    }

    fun doNothing(): Consumer<Throwable> = Consumer { Timber.tag(RxError::class.java.name).d(it, "Something went wrong") }
}