package ru.rovkinmax.tochkatest.feature.userlist.domain

import io.reactivex.Completable
import io.reactivex.Single
import ru.rovkinmax.tochkatest.feature.global.domain.UserInfoEntity
import ru.rovkinmax.tochkatest.feature.userlist.data.UserRepository
import javax.inject.Inject

class UserInteractor @Inject constructor(private val repository: UserRepository) {
    fun userInfo(): Single<UserInfoEntity> {
        return repository.getUserInfo()
    }

    fun searchUser(query: String, count: Int, offset: Int): Single<List<GithubUserEntity>> {
        val page = if (offset < count) 0 else Math.ceil(offset.toDouble() / count.toDouble()).toInt()
        return repository.search(query, count, page)
    }

    fun logout(): Completable {
        return repository.logOut()
    }
}