package com.dicoding.asclepius.model

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dicoding.asclepius.data.ArticlesItem
import com.dicoding.asclepius.data.NewsResponse
import com.dicoding.asclepius.retrofit.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class NewsModel : ViewModel() {

    private val TAG = "NewsViewModel"

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _response = MutableLiveData<ArrayList<ArticlesItem>>()
    val response: LiveData<ArrayList<ArticlesItem>> = _response

    init {
        fetchNews()
    }

    private fun fetchNews() {
        _isLoading.value = true
        ApiConfig.getApiService().getNews().enqueue(object : Callback<NewsResponse> {
            override fun onResponse(p0: Call<NewsResponse>, p1: Response<NewsResponse>) {
                _response.value = p1.body()?.articles as ArrayList<ArticlesItem>
                _isLoading.value = false
            }

            override fun onFailure(p0: Call<NewsResponse>, p1: Throwable) {
                Log.e(TAG, p1.message.toString())
                _isLoading.value = false
            }
        })
    }
}