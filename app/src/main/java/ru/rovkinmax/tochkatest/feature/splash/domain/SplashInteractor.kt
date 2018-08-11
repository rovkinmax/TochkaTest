package ru.rovkinmax.tochkatest.feature.splash.domain

import io.reactivex.Single
import ru.rovkinmax.socialnetwork.SocialType
import ru.rovkinmax.tochkatest.feature.splash.data.SplachRepository
import javax.inject.Inject

class SplashInteractor @Inject constructor(private val repository: SplachRepository) {

    fun isAlreadyLogin(): Single<Boolean> {
        return repository.currentSocialType()
                .map { it != SocialType.NONE }
    }
}