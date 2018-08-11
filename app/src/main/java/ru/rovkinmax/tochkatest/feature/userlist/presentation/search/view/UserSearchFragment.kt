package ru.rovkinmax.tochkatest.feature.userlist.presentation.search.view

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.SearchView
import com.jakewharton.rxbinding2.widget.RxSearchView
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Consumer
import kotlinx.android.synthetic.main.error_view.*
import kotlinx.android.synthetic.main.fmt_user_search.*
import ru.rovkinmax.tochkatest.R
import ru.rovkinmax.tochkatest.di.DI
import ru.rovkinmax.tochkatest.feature.global.presentation.RxError
import ru.rovkinmax.tochkatest.feature.global.presentation.presenter.Presenter
import ru.rovkinmax.tochkatest.feature.global.presentation.view.BaseFragment
import ru.rovkinmax.tochkatest.feature.global.presentation.view.BaseView
import ru.rovkinmax.tochkatest.feature.userlist.domain.GithubUserEntity
import ru.rovkinmax.tochkatest.feature.userlist.presentation.search.presenter.UserSearchPresenter
import ru.rovkinmax.tochkatest.feature.userlist.presentation.search.view.adapter.GithubUserListAdapter
import ru.rovkinmax.tochkatest.model.system.rx.RxSchedulers
import ru.rovkinmax.tochkatest.model.system.rx.subscribe
import ru.rovkinmax.tochkatest.model.ui.extentions.gone
import ru.rovkinmax.tochkatest.model.ui.extentions.show
import ru.rovkinmax.tochkatest.model.ui.pagination.paginationObservable
import toothpick.Toothpick
import java.util.concurrent.TimeUnit

class UserSearchFragment : BaseFragment(), UserSearchView {

    companion object {
        private const val SEARCH_DEBOUNCE = 600L
        fun newInstance(): UserSearchFragment {
            return UserSearchFragment().apply {
                arguments = Bundle()
            }
        }
    }

    override val layoutRes: Int = R.layout.fmt_user_search
    override val title: Int = R.string.users_title

    private val presenter by lazy { Toothpick.openScope(DI.SCOPE_FLOW_USERS).getInstance(UserSearchPresenter::class.java) }
    private val adapter = GithubUserListAdapter(this::onItemClick)
    private var paginationDisposable: Disposable? = null
    private var searchView: SearchView? = null


    @Suppress("UNCHECKED_CAST")
    override fun providePresenter(): Presenter<in BaseView> = presenter as Presenter<in BaseView>

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = adapter
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater?.inflate(R.menu.user_search, menu)
        menu?.findItem(R.id.search)?.let { item ->
            (item.actionView as? SearchView?)?.let(this::onSearchViewReady)
        }
    }

    private fun onSearchViewReady(searchView: SearchView) {
        this.searchView = searchView
        RxSearchView.queryTextChanges(searchView)
                .map(CharSequence::toString)
                .debounce(SEARCH_DEBOUNCE, TimeUnit.MILLISECONDS)
                .observeOn(RxSchedulers.main())
                .subscribe(presenter::onSearchInputChanged, RxError.doNothing())
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.exit -> presenter.logout()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun showUserList(userList: List<GithubUserEntity>) {
        recyclerView.show()
        adapter.changeDataSet(userList)
        if (paginationDisposable?.isDisposed == false)
            paginationDisposable?.dispose()
        paginationDisposable = providePaginationDisposable()
    }

    override fun showEmptyStub() {
        tvEmpty.show()
    }

    override fun hideEmptyStub() {
        tvEmpty.gone()
    }

    private fun providePaginationDisposable(): Disposable {
        return recyclerView.paginationObservable(UserSearchPresenter.SIZE_PAGE)
                .subscribe(Consumer { presenter.loadNewPageWithOffset(adapter.getRealItemCount()) }, RxError.doNothing())
    }


    override fun showLoadingIndicator() {
        errorContainer.gone()
        tvEmpty.gone()
        progress.show()
    }

    override fun hideLoadingIndicator() {
        progress.gone()
    }

    override fun showPaginationLoading() {
        adapter.showLoading()
    }

    override fun hidePaginationLoading() {
        //do nothing
    }

    override fun showPageError(message: String) {
        adapter.showError(message) {
            presenter.loadNewPageWithOffset(adapter.getRealItemCount())
        }
    }

    override fun hidePageError() {
        adapter.hideError()
    }

    override fun showNewPageUsers(userList: List<GithubUserEntity>) {
        adapter.addDataSet(userList)
    }

    override fun showErrorMessage(message: String, needCallback: Boolean) {
        tvEmpty.gone()
        recyclerView.gone()
        errorContainer.show()
        tvError.text = message
        btnRetry.setOnClickListener { presenter.onSearchInputChanged(searchView?.query?.toString().orEmpty()) }
    }

    private fun onItemClick(user: GithubUserEntity) {
        presenter.onItemClick(user)
    }
}