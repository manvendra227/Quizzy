package com.example.quizzy.Service

import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create

object RetrofitBuilder {

    private const val baseURL = "https://99c7-43-230-39-38.in.ngrok.io/"

    private val okHttp = OkHttpClient.Builder()

    private val builder = Retrofit.Builder().baseUrl(baseURL)
        .addConverterFactory(GsonConverterFactory.create())
        .client(okHttp.build())

    private val retrofit = builder.build()

    fun <T> buildService(service: Class<T>):T{
        return retrofit.create(service)
    }
}