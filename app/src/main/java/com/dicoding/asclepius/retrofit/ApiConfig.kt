package com.dicoding.asclepius.retrofit

import com.dicoding.asclepius.BuildConfig
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ApiConfig {
    companion object {
        fun getApiService(): ApiService {
            val interceptor = Interceptor {
                val originalRequest = it.request()
                val authRequest = originalRequest.newBuilder()
                    .header("Authorization", "Bearer ${BuildConfig.NEWS_API_KEY}")
                    .build()
                return@Interceptor it.proceed(authRequest)
            }
            val okHttpClient = OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .build()
            return Retrofit.Builder()
                .baseUrl("https://newsapi.org/")
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(ApiService::class.java)
        }
    }
}