package ru.rovkinmax.tochkatest.feature.splash.data

import io.reactivex.Single
import ru.rovkinmax.socialnetwork.SocialType
import ru.rovkinmax.tochkatest.model.system.prefs.Preferences
import javax.inject.Inject

class SplachRepository @Inject constructor(private val preferences: Preferences) {

    fun currentSocialType(): Single<SocialType> {
        return Single.create { emitter ->
            emitter.onSuccess(preferences.socialType)
        }
    }
}