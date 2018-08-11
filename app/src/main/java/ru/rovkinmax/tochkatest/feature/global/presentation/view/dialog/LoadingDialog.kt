package ru.rovkinmax.tochkatest.feature.global.presentation.view.dialog


import android.app.Dialog
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v7.app.AlertDialog
import ru.rovkinmax.tochkatest.R
import ru.rovkinmax.tochkatest.feature.global.presentation.view.LoadingView
import ru.rovkinmax.tochkatest.model.ui.extentions.showFragment

class LoadingDialog : DialogFragment() {
    init {
        setStyle(STYLE_NORMAL, R.style.AppTheme)
    }


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return AlertDialog.Builder(activity!!)
                .setView(R.layout.dialog_loading)
                .create()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        isCancelable = false
    }


    companion object {

        fun view(fm: FragmentManager): LoadingView {
            return object : LoadingView {
                private val tag = LoadingDialog::class.java.name


                override fun showLoadingIndicator() {
                    fm.showFragment(LoadingDialog(), tag = tag)
                }

                override fun hideLoadingIndicator() {
                    val dialog = (fm.findFragmentByTag(tag) as LoadingDialog?)
                    dialog?.dismissAllowingStateLoss()
                }
            }
        }

        fun view(fragment: Fragment): LoadingView {
            return view(fragment.fragmentManager!!)
        }
    }
}