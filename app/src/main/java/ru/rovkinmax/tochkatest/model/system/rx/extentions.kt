package ru.rovkinmax.tochkatest.model.system.rx

import android.annotation.SuppressLint
import io.reactivex.*
import io.reactivex.functions.Action
import io.reactivex.functions.Consumer
import ru.rovkinmax.tochkatest.feature.global.presentation.RxDecor
import ru.rovkinmax.tochkatest.feature.global.presentation.view.LoadingView

fun <T> Observable<T>.async(): Observable<T> = subscribeOn(RxSchedulers.io()).observeOn(RxSchedulers.main())

fun <T> Flowable<T>.async(): Flowable<T> = subscribeOn(RxSchedulers.io()).observeOn(RxSchedulers.main())

fun <T> Single<T>.async(): Single<T> = subscribeOn(RxSchedulers.io()).observeOn(RxSchedulers.main())

fun <T> Maybe<T>.async(): Maybe<T> = subscribeOn(RxSchedulers.io()).observeOn(RxSchedulers.main())

fun Completable.async(): Completable = subscribeOn(RxSchedulers.io()).observeOn(RxSchedulers.main())


fun <T> Observable<T>.loading(loadingView: LoadingView): Observable<T> = compose(RxDecor.loading(loadingView))

fun <T> Flowable<T>.loading(loadingView: LoadingView): Flowable<T> = compose(RxDecor.loading(loadingView))

fun <T> Single<T>.loading(loadingView: LoadingView): Single<T> = compose(RxDecor.loading(loadingView))

fun <T> Maybe<T>.loading(loadingView: LoadingView): Maybe<T> = compose(RxDecor.loading(loadingView))

fun Completable.loading(loadingView: LoadingView): Completable = compose(RxDecor.loading<Any>(loadingView))


@SuppressLint("CheckResult")
fun <T> Observable<T>.subscribe(func: (T) -> Unit, consumer: Consumer<Throwable>) {
    subscribe(Consumer<T> { func(it) }, consumer)
}

@SuppressLint("CheckResult")
fun <T> Flowable<T>.subscribe(func: (T) -> Unit, consumer: Consumer<Throwable>) {
    subscribe(Consumer<T> { func(it) }, consumer)
}

@SuppressLint("CheckResult")
fun <T> Single<T>.subscribe(func: (T) -> Unit, consumer: Consumer<Throwable>) {
    subscribe(Consumer<T> { func(it) }, consumer)
}

@SuppressLint("CheckResult")
fun <T> Maybe<T>.subscribe(func: (T) -> Unit, consumer: Consumer<Throwable>) {
    subscribe(Consumer<T> { func(it) }, consumer)
}

@SuppressLint("CheckResult")
fun Completable.subscribe(func: () -> Unit, consumer: Consumer<Throwable>) {
    subscribe(Action { func() }, consumer)
}