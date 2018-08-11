package ru.rovkinmax.tochkatest

import android.app.Application
import com.facebook.FacebookSdk
import com.facebook.appevents.AppEventsLogger
import com.vk.sdk.VKSdk
import ru.rovkinmax.tochkatest.di.DI
import timber.log.Timber
import toothpick.Toothpick
import toothpick.configuration.Configuration
import toothpick.registries.FactoryRegistryLocator
import toothpick.registries.MemberInjectorRegistryLocator


class App : Application() {

    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) Timber.plant(Timber.DebugTree())
        initDi()
        VKSdk.initialize(this)
        FacebookSdk.sdkInitialize(applicationContext)
        AppEventsLogger.activateApp(this)
    }

    private fun initDi() {
        val configuration = if (BuildConfig.DEBUG)
            Configuration.forDevelopment().preventMultipleRootScopes().disableReflection()
        else Configuration.forProduction().disableReflection()

        Toothpick.setConfiguration(configuration)
        FactoryRegistryLocator.setRootRegistry(ru.rovkinmax.tochkatest.FactoryRegistry())
        MemberInjectorRegistryLocator.setRootRegistry(ru.rovkinmax.tochkatest.MemberInjectorRegistry())

        DI.initAppScope(context = this)
    }
}