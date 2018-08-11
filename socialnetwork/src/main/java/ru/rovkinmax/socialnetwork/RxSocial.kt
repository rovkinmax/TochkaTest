package ru.rovkinmax.socialnetwork

import android.support.v4.app.FragmentManager
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single

abstract class RxSocial(private val fragmentManager: FragmentManager) {
    private val tag: String = javaClass.name
    protected val fragment: RxSocialFragment = getOrCreateFragment()


    private fun getOrCreateFragment(): RxSocialFragment {
        var fragment = findFragment()
        if (fragment == null) {
            fragment = createFragment()
            fragmentManager.beginTransaction()
                    ?.add(fragment, tag)
                    ?.commitAllowingStateLoss()
        }
        return fragment
    }

    protected abstract fun createFragment(): RxSocialFragment
    private fun findFragment(): RxSocialFragment? = fragmentManager.findFragmentByTag(tag) as? RxSocialFragment
    fun login(): Observable<SocialLoginResult> {
        return Completable.create { emitter ->
            emitter.onComplete()
            fragment.startLogin()
        }.andThen(fragment.subject)
                .doOnError { fragment.onError() }
    }

    abstract fun getUserInfo(): Single<UserInfo>

    abstract fun logout(): Completable
}