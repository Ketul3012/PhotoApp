import androidx.paging.LivePagedListBuilder
import com.example.photoapp.ImageDataSourceFactory
import com.example.photoapp.ImageResponse

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.paging.PageKeyedDataSource
import androidx.paging.PagedList


class ImageViewModel : ViewModel {
    var itemPagedList: LiveData<PagedList<ImageResponse.ImageResponseItem.Urls>>? = null
    var liveDataSource: LiveData<PageKeyedDataSource<Int, ImageResponse.ImageResponseItem.Urls>>? = null

    constructor(){
        val dataSource = ImageDataSourceFactory()
        liveDataSource = dataSource.getLiveData()
        val pagedConfig = PagedList.Config.Builder().setEnablePlaceholders(false).setPageSize(10).build()
        itemPagedList = LivePagedListBuilder(dataSource,pagedConfig ).build()
    }

}
