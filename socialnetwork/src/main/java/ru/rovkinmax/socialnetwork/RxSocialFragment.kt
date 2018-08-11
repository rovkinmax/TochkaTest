package ru.rovkinmax.socialnetwork

import android.os.Bundle
import android.support.v4.app.Fragment
import io.reactivex.subjects.PublishSubject
import io.reactivex.subjects.Subject

abstract class RxSocialFragment : Fragment() {

    internal var subject: Subject<SocialLoginResult> = PublishSubject.create<SocialLoginResult>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true
    }

    protected abstract fun login()
    fun startLogin() {
        login()
    }

    fun onError() {
        subject = PublishSubject.create<SocialLoginResult>()
    }
}