package com.dicoding.asclepius.retrofit

import com.dicoding.asclepius.BuildConfig
import com.dicoding.asclepius.data.NewsResponse
import retrofit2.Call
import retrofit2.http.GET

interface ApiService {
    @GET("v2/top-headlines?q=cancer&category=health&language=en")
    fun getNews(): Call<NewsResponse>
}
