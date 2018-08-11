package ru.rovkinmax.tochkatest.feature.auth.domain

import io.reactivex.Observable
import ru.rovkinmax.socialnetwork.SocialLoginResult
import ru.rovkinmax.tochkatest.feature.auth.data.LoginRepository
import javax.inject.Inject

class LoginInteractor @Inject constructor(private val repository: LoginRepository) {
    fun loginVk(): Observable<SocialLoginResult> {
        return repository.loginVk()
                .flatMap { saveSocialType(it) }
    }

    fun loginFb(): Observable<SocialLoginResult> {
        return repository.loginFb()
                .flatMap { saveSocialType(it) }

    }

    fun loginPlus(): Observable<SocialLoginResult> {
        return repository.loginPlus()
                .flatMap { saveSocialType(it) }
    }

    private fun saveSocialType(it: SocialLoginResult): Observable<SocialLoginResult> {
        return repository.saveSocialType(it.socialType)
                .andThen(Observable.just(it))
    }

}
