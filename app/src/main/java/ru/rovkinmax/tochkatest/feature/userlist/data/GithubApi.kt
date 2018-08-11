package ru.rovkinmax.tochkatest.feature.userlist.data

import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface GithubApi {

    @GET("search/users")
    fun search(@Query("q") query: String,
               @Query("per_page") count: Int,
               @Query("page") pageNumber: Int): Single<GithubSearchResponse>
}