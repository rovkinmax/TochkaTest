package ru.rovkinmax.tochkatest.di

import android.support.v4.app.FragmentManager
import com.google.gson.Gson
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import ru.rovkinmax.socialnetwork.RxSocial
import ru.rovkinmax.socialnetwork.SocialType
import ru.rovkinmax.socialnetwork.fb.RxFb
import ru.rovkinmax.socialnetwork.plus.RxPlus
import ru.rovkinmax.socialnetwork.vk.RxVk
import ru.rovkinmax.tochkatest.BuildConfig
import ru.rovkinmax.tochkatest.feature.userlist.data.GithubApi
import ru.rovkinmax.tochkatest.model.system.flow.FlowRouter
import ru.rovkinmax.tochkatest.model.system.flow.GlobalRouter
import ru.rovkinmax.tochkatest.model.system.network.CurlLoggingInterceptor
import ru.rovkinmax.tochkatest.model.system.prefs.Preferences
import ru.rovkinmax.tochkatest.model.system.rx.RxSchedulers
import ru.terrakok.cicerone.Cicerone
import ru.terrakok.cicerone.NavigatorHolder
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Provider


class LocalRouterProvider @Inject constructor(private val router: GlobalRouter) : Provider<FlowRouter> {
    override fun get(): FlowRouter = FlowRouter(router)
}

class LocalCiceroneProvider @Inject constructor(private val router: FlowRouter) : Provider<Cicerone<FlowRouter>> {
    override fun get(): Cicerone<FlowRouter> = Cicerone.create(router)
}

class LocalNavigatorProvider @Inject constructor(private val cicerone: Cicerone<FlowRouter>) : Provider<NavigatorHolder> {
    override fun get(): NavigatorHolder = cicerone.navigatorHolder
}

class RxSocialProvider @Inject constructor(private val preferences: Preferences,
                                           private val fragmentManager: FragmentManager) : Provider<RxSocial> {
    override fun get(): RxSocial {
        return when (preferences.socialType) {
            SocialType.VK -> RxVk(fragmentManager)
            SocialType.FB -> RxFb(fragmentManager)
            SocialType.PLUS -> RxPlus(fragmentManager)
            else -> throw IllegalStateException("Unknown social type")
        }
    }
}


class OkHttpProvider @Inject constructor() : Provider<OkHttpClient> {

    companion object {
        private const val TIMEOUT_CONNECTION = 30L
        private const val TIMEOUT_READ = 30L
        private const val TIMEOUT_WRITE = 30L
    }

    override fun get(): OkHttpClient {
        return OkHttpClient.Builder()
                .connectTimeout(TIMEOUT_CONNECTION, TimeUnit.SECONDS)
                .readTimeout(TIMEOUT_READ, TimeUnit.SECONDS)
                .writeTimeout(TIMEOUT_WRITE, TimeUnit.SECONDS)
                .addInterceptor(CurlLoggingInterceptor())
                .build()
    }
}

class RetrofitProvider @Inject constructor(private val okHttpClient: OkHttpClient,
                                           private val gson: Gson) : Provider<Retrofit> {
    override fun get(): Retrofit {
        return Retrofit.Builder()
                .baseUrl(BuildConfig.ENDPOINT)
                .client(okHttpClient)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(RxSchedulers.io()))
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build()
    }
}

class GithubApiProvider @Inject constructor(private val retrofit: Retrofit) : Provider<GithubApi> {
    override fun get(): GithubApi {
        return retrofit.create(GithubApi::class.java)
    }
}

