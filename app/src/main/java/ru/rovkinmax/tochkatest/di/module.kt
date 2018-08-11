package ru.rovkinmax.tochkatest.di

import android.content.Context
import com.google.gson.Gson
import ru.rovkinmax.tochkatest.feature.global.presentation.DefaultErrorHandler
import ru.rovkinmax.tochkatest.feature.global.presentation.ErrorHandler
import ru.rovkinmax.tochkatest.model.AndroidResourceProvider
import ru.rovkinmax.tochkatest.model.ResourceProvider
import ru.rovkinmax.tochkatest.model.system.flow.FlowRouter
import ru.rovkinmax.tochkatest.model.system.flow.GlobalRouter
import ru.rovkinmax.tochkatest.model.system.prefs.Preferences
import ru.terrakok.cicerone.Cicerone
import ru.terrakok.cicerone.NavigatorHolder
import toothpick.config.Module

fun module(func: (Module.() -> (Unit))) = object : Module() {
    init {
        func()
    }
}

fun moduleApp(context: Context): Module = module {
    bind(Context::class.java).toInstance(context.applicationContext)
    bind(Preferences::class.java).singletonInScope()
    bind(Gson::class.java).toInstance(Gson())
    bind(ResourceProvider::class.java).to(AndroidResourceProvider::class.java).singletonInScope()
    bind(ErrorHandler::class.java).to(DefaultErrorHandler::class.java).singletonInScope()

    val cicerone = Cicerone.create(GlobalRouter())
    bind(GlobalRouter::class.java).toInstance(cicerone.router)
    bind(NavigatorHolder::class.java).toInstance(cicerone.navigatorHolder)
}

fun flowModule(func: Module.() -> Unit) = module {
    bind(FlowRouter::class.java).toProvider(LocalRouterProvider::class.java).providesSingletonInScope()
    bind(Cicerone::class.java).toProvider(LocalCiceroneProvider::class.java).singletonInScope()
    bind(NavigatorHolder::class.java).toProvider(LocalNavigatorProvider::class.java).providesSingletonInScope()
    func.invoke(this)
}
