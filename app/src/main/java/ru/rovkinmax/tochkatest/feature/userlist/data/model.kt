package ru.rovkinmax.tochkatest.feature.userlist.data

data class GithubUserDto(
        val id: String?,
        val login: String?,
        val avatar_url: String?,
        val html_url: String?
)

data class GithubSearchResponse(val items: List<GithubUserDto>)