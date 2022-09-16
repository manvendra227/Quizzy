package com.example.quizzy.Service

import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory


object RetrofitBuilder {

    private const val baseURL = "https://c6ac-103-72-6-20.in.ngrok.io/"

    private val okHttp = OkHttpClient.Builder()

    private var gson = GsonBuilder()
        .setLenient()
        .create()
    private val builder = Retrofit.Builder().baseUrl(baseURL)
        .addConverterFactory(ScalarsConverterFactory.create())
        .addConverterFactory(GsonConverterFactory.create(gson))
        .client(okHttp.build())

    private val retrofit = builder.build()

    fun <T> buildService(service: Class<T>):T{
        return retrofit.create(service)
    }
}