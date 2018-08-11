package ru.rovkinmax.tochkatest.model.system.rx

import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers


object RxSchedulers {

    fun io(): Scheduler {
        return Schedulers.io()
    }

    fun computation(): Scheduler = Schedulers.computation()

    fun main(): Scheduler {
        return AndroidSchedulers.mainThread()
    }
}