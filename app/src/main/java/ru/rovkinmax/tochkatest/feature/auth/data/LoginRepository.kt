package ru.rovkinmax.tochkatest.feature.auth.data

import io.reactivex.Completable
import io.reactivex.Observable
import ru.rovkinmax.socialnetwork.SocialLoginResult
import ru.rovkinmax.socialnetwork.SocialType
import ru.rovkinmax.socialnetwork.fb.RxFb
import ru.rovkinmax.socialnetwork.plus.RxPlus
import ru.rovkinmax.socialnetwork.vk.RxVk
import ru.rovkinmax.tochkatest.model.system.prefs.Preferences
import javax.inject.Inject

class LoginRepository @Inject constructor(private val vkLogin: RxVk,
                                          private val fbLogin: RxFb,
                                          private val plusLogin: RxPlus,
                                          private val preferences: Preferences) {
    fun loginVk(): Observable<SocialLoginResult> = vkLogin.login()

    fun loginFb(): Observable<SocialLoginResult> = fbLogin.login()

    fun loginPlus(): Observable<SocialLoginResult> = plusLogin.login()

    fun saveSocialType(socialType: SocialType): Completable {
        return Completable.create { emitter ->
            preferences.socialType = socialType
            emitter.onComplete()
        }
    }
}