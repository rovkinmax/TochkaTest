package ru.rovkinmax.tochkatest.feature.global.presentation.view.dialog

import android.app.Dialog
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v4.app.FragmentManager
import android.support.v7.app.AlertDialog
import ru.rovkinmax.tochkatest.R
import ru.rovkinmax.tochkatest.model.ui.extentions.showFragment

class ErrorMessageDialog : DialogFragment() {

    companion object {
        fun show(fragmentManager: FragmentManager,
                 titleResId: Int = R.string.error_title,
                 message: CharSequence, func: (() -> Unit)?) {
            val dialog = ErrorMessageDialog().apply {
                this.titleResId = titleResId
                this.message = message
                dismissCallback = func

            }
            fragmentManager.showFragment(dialog)
        }
    }

    init {
        setStyle(STYLE_NORMAL, R.style.AppTheme)
    }

    private var dismissCallback: (() -> Unit)? = null
    private var titleResId: Int = R.string.empty
    private var message: CharSequence = ""
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return AlertDialog.Builder(activity!!)
                .setTitle(titleResId)
                .setMessage(message)
                .setPositiveButton(R.string.button_close) { _, _ -> dismissCallback?.invoke() }
                .create()
    }
}