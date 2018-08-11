package ru.rovkinmax.tochkatest.feature.userlist.domain

import java.io.Serializable

data class GithubUserEntity(val id: String,
                            val login: String,
                            val avatarUrl: String,
                            val htmlUrl: String) : Serializable