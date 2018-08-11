package ru.rovkinmax.tochkatest.feature.userlist.data

import ru.rovkinmax.socialnetwork.UserInfo
import ru.rovkinmax.tochkatest.feature.global.domain.UserInfoEntity
import ru.rovkinmax.tochkatest.feature.userlist.domain.GithubUserEntity

fun UserInfo.toEntity(): UserInfoEntity = UserInfoEntity(username, avatar.orEmpty())

fun GithubUserDto.toEntity(): GithubUserEntity {
    return GithubUserEntity(id.orEmpty(),
            login.orEmpty(),
            avatar_url.orEmpty(),
            html_url.orEmpty())
}