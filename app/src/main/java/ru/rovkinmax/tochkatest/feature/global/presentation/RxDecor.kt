package ru.rovkinmax.tochkatest.feature.global.presentation


import io.reactivex.*
import org.reactivestreams.Publisher
import ru.rovkinmax.tochkatest.feature.global.presentation.view.EmptyView
import ru.rovkinmax.tochkatest.feature.global.presentation.view.ErrorView
import ru.rovkinmax.tochkatest.feature.global.presentation.view.LoadingView
import ru.rovkinmax.tochkatest.feature.global.presentation.view.PaginationView

object RxDecor {

    fun <T> loading(view: LoadingView): LoadingViewTransformer<T, T> = LoadingViewTransformer(view)

    fun <T> paginationLoading(view: PaginationView): LoadingViewTransformer<T, T> {
        val loadingView = object : LoadingView {
            override fun showLoadingIndicator() {
                view.showPaginationLoading()
            }

            override fun hideLoadingIndicator() {
                view.hidePaginationLoading()
            }
        }
        return loading(loadingView)
    }


    fun <T> emptyStub(view: EmptyView): EmptyStubTransformer<T, T> = EmptyStubTransformer(view)

    class EmptyStubTransformer<T : R, R>(private val view: EmptyView) : ObservableTransformer<T, R>, FlowableTransformer<T, R> {

        override fun apply(upstream: Flowable<T>): Publisher<R> {
            return upstream.doOnSubscribe { view.hideEmptyStub() }.switchIfEmpty(emptyFlowable(view)).map { it }
        }

        override fun apply(upstream: Observable<T>): ObservableSource<R> {
            return upstream.doOnSubscribe { view.hideEmptyStub() }.switchIfEmpty(emptyObservable(view)).map { it }
        }

        private fun <T> emptyObservable(view: EmptyView): Observable<T> {
            return Observable.create<T>({ it.onComplete() }).doOnComplete({ view.showEmptyStub() })
        }

        private fun <T> emptyFlowable(view: EmptyView): Flowable<T> {
            return Flowable.create<T>({ it.onComplete() }, BackpressureStrategy.ERROR).doOnComplete { view.showEmptyStub() }
        }
    }

    class LoadingViewTransformer<T : R, R>(private val loadingView: LoadingView) : ObservableTransformer<T, R>,
            SingleTransformer<T, R>, FlowableTransformer<T, R>, CompletableTransformer, MaybeTransformer<T, R> {

        override fun apply(upstream: Completable): CompletableSource {
            return upstream
                    .doOnSubscribe { loadingView.showLoadingIndicator() }
                    .doFinally { loadingView.hideLoadingIndicator() }
        }

        override fun apply(upstream: Flowable<T>): Publisher<R> {
            return upstream
                    .doOnSubscribe { loadingView.showLoadingIndicator() }
                    .doFinally { loadingView.hideLoadingIndicator() }
                    .map { it }
        }

        override fun apply(upstream: Single<T>): SingleSource<R> {
            return upstream
                    .doOnSubscribe { loadingView.showLoadingIndicator() }
                    .doFinally { loadingView.hideLoadingIndicator() }
                    .map { it }
        }

        override fun apply(upstream: Observable<T>): ObservableSource<R> {
            return upstream
                    .doOnSubscribe { loadingView.showLoadingIndicator() }
                    .doFinally { loadingView.hideLoadingIndicator() }
                    .map { it }
        }

        override fun apply(upstream: Maybe<T>): MaybeSource<R> {
            return upstream
                    .doOnSubscribe { loadingView.showLoadingIndicator() }
                    .doFinally { loadingView.hideLoadingIndicator() }
                    .map { it }
        }
    }
}

fun loadingView(show: (() -> (Unit)), hide: (() -> (Unit))): LoadingView = object : LoadingView {
    override fun showLoadingIndicator() = show()
    override fun hideLoadingIndicator() = hide()
}

fun errorView(showError: ((String) -> Unit), hideError: (() -> Unit)? = null): ErrorView = object : ErrorView {
    override fun showErrorMessage(message: String, needCallback: Boolean) {
        showError(message)
    }

    override fun hideErrorMessage() {
        hideError?.invoke()
    }
}