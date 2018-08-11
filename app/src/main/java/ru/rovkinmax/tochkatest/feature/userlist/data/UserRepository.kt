package ru.rovkinmax.tochkatest.feature.userlist.data

import io.reactivex.Completable
import io.reactivex.Single
import ru.rovkinmax.socialnetwork.RxSocial
import ru.rovkinmax.socialnetwork.SocialType
import ru.rovkinmax.tochkatest.feature.global.domain.UserInfoEntity
import ru.rovkinmax.tochkatest.feature.userlist.domain.GithubUserEntity
import ru.rovkinmax.tochkatest.model.system.prefs.Preferences
import javax.inject.Inject

class UserRepository @Inject constructor(private val rxSocial: RxSocial,
                                         private val api: GithubApi,
                                         private val preferences: Preferences) {

    fun getUserInfo(): Single<UserInfoEntity> {
        return rxSocial.getUserInfo()
                .map { it.toEntity() }
    }

    fun search(query: String, count: Int, page: Int): Single<List<GithubUserEntity>> {
        return api.search(query, count, page)
                .map { it.items }
                .map { it.map { it.toEntity() } }
    }

    fun logOut(): Completable {
        return rxSocial.logout()
                .doOnComplete { preferences.socialType = SocialType.NONE }
    }
}