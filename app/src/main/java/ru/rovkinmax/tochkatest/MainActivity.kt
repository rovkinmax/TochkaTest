package ru.rovkinmax.tochkatest

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import com.vk.sdk.util.VKUtil
import ru.rovkinmax.tochkatest.di.DI
import ru.rovkinmax.tochkatest.di.inject
import ru.rovkinmax.tochkatest.feature.global.FlowFragment
import ru.rovkinmax.tochkatest.model.system.flow.FragmentNavigator
import ru.rovkinmax.tochkatest.model.system.flow.GlobalRouter
import ru.terrakok.cicerone.NavigatorHolder
import toothpick.Toothpick
import javax.inject.Inject

class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var navigatorHolder: NavigatorHolder

    @Inject
    lateinit var router: GlobalRouter

    private val navigator by lazy { provideNavigator() }

    private fun provideNavigator(): FragmentNavigator {
        return object : FragmentNavigator(applicationContext, supportFragmentManager, R.id.flowContainer) {
            override fun createFragment(screenKey: String, data: Any?): Fragment? = Screens.getFlowFragment(screenKey, data)

            override fun exit() {
                finish()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        Toothpick.openScope(DI.SCOPE_APP).inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val fingerprints = VKUtil.getCertificateFingerprint(this, this.packageName)
        if (savedInstanceState == null)
            navigator.setLaunchScreen(Screens.FLOW_SPLASH)


    }

    override fun onResumeFragments() {
        super.onResumeFragments()
        navigatorHolder.setNavigator(navigator)
    }

    override fun onPause() {
        navigatorHolder.removeNavigator()
        super.onPause()
    }

    override fun onBackPressed() {
        val fragment = supportFragmentManager.findFragmentById(R.id.flowContainer)
        (fragment as? FlowFragment)?.onBackPressed()
    }
}