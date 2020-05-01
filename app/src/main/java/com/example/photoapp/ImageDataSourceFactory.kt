package com.example.photoapp

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import androidx.paging.PageKeyedDataSource


class ImageDataSourceFactory : DataSource.Factory<Int , ImageResponse.ImageResponseItem.Urls>() {
    private val itemLiveDataSource = MutableLiveData<PageKeyedDataSource<Int, ImageResponse.ImageResponseItem.Urls>>()

    override fun create(): DataSource<Int, ImageResponse.ImageResponseItem.Urls> {
        val imageDataSource = ImageDataSource()
        itemLiveDataSource.postValue(imageDataSource)
        return imageDataSource
    }

    fun getLiveData(): MutableLiveData<PageKeyedDataSource<Int, ImageResponse.ImageResponseItem.Urls>> {
        return itemLiveDataSource
    }
}