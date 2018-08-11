package ru.rovkinmax.tochkatest.feature.auth.presentation.view

import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.fmt_login.*
import ru.rovkinmax.tochkatest.R
import ru.rovkinmax.tochkatest.di.DI
import ru.rovkinmax.tochkatest.feature.auth.presentation.presenter.LoginPresenter
import ru.rovkinmax.tochkatest.feature.global.presentation.presenter.Presenter
import ru.rovkinmax.tochkatest.feature.global.presentation.view.BaseFragment
import ru.rovkinmax.tochkatest.feature.global.presentation.view.BaseView
import toothpick.Toothpick

class LoginFragment : BaseFragment(), LoginView {

    companion object {
        fun newInstance(): LoginFragment {
            return LoginFragment().apply {
                arguments = Bundle()
            }
        }
    }

    override val layoutRes: Int = R.layout.fmt_login

    private val presenter: LoginPresenter by lazy {
        Toothpick.openScope(DI.SCOPE_FLOW_AUTH).getInstance(LoginPresenter::class.java)
    }

    @Suppress("UNCHECKED_CAST")
    override fun providePresenter(): Presenter<BaseView> = presenter as Presenter<BaseView>

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        btnVk.setOnClickListener { presenter.onLoginVkClicked() }
        btnFb.setOnClickListener { presenter.onLoginFbClicked() }
        btnPlus.setOnClickListener { presenter.onLoginPlusClicked() }
    }
}