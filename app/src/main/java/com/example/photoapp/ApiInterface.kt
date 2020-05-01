package com.example.photoapp

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiInterface {

    @GET("photos")
    fun getAllImages( @Query("client_id") id : String , @Query("page") page : String) : Call<ImageResponse>
}