package com.example.photoapp

import androidx.paging.PageKeyedDataSource
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ImageDataSource : PageKeyedDataSource<Int , ImageResponse.ImageResponseItem.Urls>() {

    var FIRST_PAGE : Int =1
    val  key = ""
    override fun loadInitial(params: LoadInitialParams<Int>, callback: LoadInitialCallback<Int, ImageResponse.ImageResponseItem.Urls>) {
            ApiClient.getApi().getAllImages(key , FIRST_PAGE.toString()).enqueue( object : Callback<ImageResponse>{
                override fun onFailure(call: Call<ImageResponse>, t: Throwable) {
                }

                override fun onResponse(call: Call<ImageResponse>, response: Response<ImageResponse>) {
                    if (response.body() != null)
                    {
                        var list : ArrayList<ImageResponse.ImageResponseItem.Urls> = ArrayList()
                        for (va in response.body()!!.iterator())
                        {
                            list.add(va.urls!!)
                        }
                        callback.onResult(  list, null, FIRST_PAGE +   1)
                    }
                }
            })
    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, ImageResponse.ImageResponseItem.Urls>) {
        ApiClient.getApi().getAllImages(key , FIRST_PAGE.toString()).enqueue( object : Callback<ImageResponse>{
            override fun onFailure(call: Call<ImageResponse>, t: Throwable) {
            }

            override fun onResponse(call: Call<ImageResponse>, response: Response<ImageResponse>) {
                if (response.body() != null)
                {
                    var list : ArrayList<ImageResponse.ImageResponseItem.Urls> = ArrayList()

                    for (va in response.body()!!.iterator())
                    {
                        list.add(va.urls!!)
                    }
                    callback.onResult(  list,params.key + 1 )
                }
            }
        })

    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, ImageResponse.ImageResponseItem.Urls>) {
        ApiClient.getApi().getAllImages(key , FIRST_PAGE.toString()).enqueue( object : Callback<ImageResponse>{
            override fun onFailure(call: Call<ImageResponse>, t: Throwable) {
            }

            override fun onResponse(call: Call<ImageResponse>, response: Response<ImageResponse>) {
                if (response.body() != null)
                {
                    var list : ArrayList<ImageResponse.ImageResponseItem.Urls> = ArrayList()
                    var adjectKey = if (params.key > 0){ params.key} else {null}
                    for (va in response.body()!!.iterator())
                    {
                        list.add(va.urls!!)
                    }
                    callback.onResult(  list,adjectKey )
                }
            }
        })
    }
}