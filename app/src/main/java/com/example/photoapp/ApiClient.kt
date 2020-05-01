package com.example.photoapp

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiClient {


      fun getClient(): Retrofit {
        return Retrofit.Builder()
                .baseUrl("https://api.unsplash.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
    }

    fun  getApi(): ApiInterface {
        return getClient().create(ApiInterface::class.java)
    }
}