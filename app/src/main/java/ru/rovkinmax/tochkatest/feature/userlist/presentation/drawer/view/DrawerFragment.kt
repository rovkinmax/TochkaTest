package ru.rovkinmax.tochkatest.feature.userlist.presentation.drawer.view

import android.net.Uri
import android.os.Bundle
import android.view.View
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fmt_drawer.*
import kotlinx.android.synthetic.main.item_header.view.*
import ru.rovkinmax.tochkatest.R
import ru.rovkinmax.tochkatest.di.DI
import ru.rovkinmax.tochkatest.feature.global.presentation.presenter.Presenter
import ru.rovkinmax.tochkatest.feature.global.presentation.view.BaseFragment
import ru.rovkinmax.tochkatest.feature.global.presentation.view.BaseView
import ru.rovkinmax.tochkatest.feature.userlist.presentation.drawer.presenter.DrawerPresenter
import toothpick.Toothpick

class DrawerFragment : BaseFragment(), DrawerView {

    override val layoutRes: Int = R.layout.fmt_drawer

    private val presenter by lazy { Toothpick.openScope(DI.SCOPE_FLOW_USERS).getInstance(DrawerPresenter::class.java) }

    @Suppress("UNCHECKED_CAST")
    override fun providePresenter(): Presenter<in BaseView> {
        return presenter as Presenter<in BaseView>
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navigationView.getHeaderView(0)
    }

    override fun showAvatar(avatar: String) {
        Picasso.get()
                .load(Uri.parse(avatar))
                .into(navigationView.getHeaderView(0).ivAvatar)
    }

    override fun showName(username: String) {
        navigationView.getHeaderView(0).tvName.text = username
    }
}