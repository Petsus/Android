package br.com.petsus.normal.api.service.others

import br.com.petsus.api.model.base.BaseResponse
import br.com.petsus.api.model.others.News
import retrofit2.Response
import retrofit2.http.GET

interface NewRepositoryRetrofit {

    @GET("news")
    suspend fun all(): Response<BaseResponse<List<News>>>

}