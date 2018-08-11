package ru.rovkinmax.tochkatest.di

import toothpick.Scope
import toothpick.Toothpick
import toothpick.config.Module

fun Scope.inject(any: Any) = Toothpick.inject(any, this)

fun Scope.module(func: Module.() -> Unit): Scope {
    installModules(ru.rovkinmax.tochkatest.di.module { func(this) })
    return this
}

fun Scope.moduleFlow(func: Module.() -> Unit): Scope {
    installModules(ru.rovkinmax.tochkatest.di.flowModule { func(this) })
    return this
}