package com.example.photoapp

import android.content.Context
import android.content.pm.PackageManager
import android.database.Cursor
import android.graphics.Color
import android.os.Bundle
import android.provider.MediaStore
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.CursorAdapter
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.view.get
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ListAdapter
import com.example.photoapp.ImageDescriptionDIalogFragment.Companion.DESCRIPTION
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.activity_images.*
import pub.devrel.easypermissions.EasyPermissions

class StorageImageActivity : AppCompatActivity() {

    private val PERMISSIONSWRITE = arrayOf( android.Manifest.permission.READ_EXTERNAL_STORAGE , android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
    private  var images : Array<String>? = arrayOf()
    private  var path : String? = null
    private   var map : HashMap<String , String> = HashMap()
    private var searchArray = ArrayList<String>()
    private  val adapter by lazy {
        StorageImageAdapter(){
            val dialogFragment = StorageImageDialogFragment()
            dialogFragment.arguments = Bundle().apply {
                putString(StorageImageDialogFragment.ARGS_URL,it)
            }
            dialogFragment.isCancelable = true
            dialogFragment.show(supportFragmentManager, StorageImageDialogFragment.TAG)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_images)
        toolbar.visibility = View.VISIBLE
        toolbar.setNavigationIcon(R.drawable.ic_keyboard_backspace_black_24dp)
        setSupportActionBar(toolbar)
        supportActionBar?.title = "Image"
        toolbar.setNavigationOnClickListener {
            onBackPressed()
        }


        path = intent.getStringExtra("PATH")
        images = getAllShownImagesPath(path.toString())

        rv_main.setHasFixedSize(true)
        rv_main.setItemViewCacheSize(20)
        val  layoutManager = GridLayoutManager(this , 2)
        rv_main.layoutManager = layoutManager

        val sharedPreferences = this.getSharedPreferences(getString(R.string.app_name) , Context.MODE_PRIVATE)
        var mapString = sharedPreferences.getString(DESCRIPTION , "").toString()
        val type = object : TypeToken<HashMap<String, String?>>() {}.type
        try {
            map = Gson().fromJson(mapString , type)
        }catch (e : java.lang.Exception)
        {}

        rv_main.adapter = adapter
        if (EasyPermissions.hasPermissions(this , PERMISSIONSWRITE[0]))
        {
            if (images?.size == 0)
            {
                images = getAllShownImagesPath(path!!)
            }
            adapter.submitData(images)
        }
    }


    override fun onResume() {
        if (EasyPermissions.hasPermissions(this , PERMISSIONSWRITE[0],PERMISSIONSWRITE[1]))
        {
            images = getAllShownImagesPath(path.toString())
            adapter.submitData(images)
        }else
        {
            EasyPermissions.requestPermissions(this ,"Please Allow Permission" , 10 ,  PERMISSIONSWRITE[0] , PERMISSIONSWRITE[1] )
        }
        super.onResume()
    }

    private fun getAllShownImagesPath(path: String): Array<String>? {
        val listOfAllImages : ArrayList<String>? = ArrayList<String>()
        val allVideosuri =
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        val projection = arrayOf(
            MediaStore.Images.ImageColumns.DATA, MediaStore.Images.Media.DISPLAY_NAME,
            MediaStore.Images.Media.SIZE
        )
        val cursor: Cursor = this.contentResolver.query(
            allVideosuri,
            projection,
            MediaStore.Images.Media.DATA + " like ? ",
            arrayOf("%$path%"),
            null
        )!!
        try {
            cursor.moveToLast()
            val index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
            do {
                listOfAllImages?.add(cursor.getString(index))
            } while (cursor.moveToPrevious())
            cursor.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return listOfAllImages?.toTypedArray()
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu);
        val menuItem = menu?.get(0)
        val searchView : SearchView
        if (menuItem != null)
        {
            searchView = menuItem.actionView as SearchView
            searchView.setOnCloseListener(SearchView.OnCloseListener {
                return@OnCloseListener true })
            searchView.queryHint = "Search with Description"
            val searchAutoComplete = searchView.findViewById<SearchView.SearchAutoComplete>(R.id.search_src_text)
            searchAutoComplete.setAdapter( SuggestionAdapter<String>(this , android.R.layout.simple_dropdown_item_1line , map.keys.toList()))
            searchAutoComplete.setOnItemClickListener { parent, view, position, id ->
                var v = (view as TextView).text
                searchView.setQuery(v , true)
            }
            searchView.setOnQueryTextListener(object  : SearchView.OnQueryTextListener{
                override fun onQueryTextSubmit(query: String?): Boolean {
                    return true
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    if (newText.equals(""))
                    {return true}
                    val key = newText?.toLowerCase()
                    searchArray.clear()
                    map.keys.forEach {
                        if (key?.let { it1 -> it.contains(it1) }!!) {
                            searchArray.add(map[it].toString())
                        }
                    }
                    adapter.submitData(searchArray.toTypedArray())
                    return true
                }

            })
            menuItem.setOnActionExpandListener( object  : MenuItem.OnActionExpandListener{
                override fun onMenuItemActionExpand(item: MenuItem?): Boolean {
                    return true
                }

                override fun onMenuItemActionCollapse(item: MenuItem?): Boolean {
                    adapter.submitData(images)
                    return true
                }
            })
        }
        return true
    }
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {

        if (grantResults[0]  == PackageManager.PERMISSION_GRANTED && requestCode == 10)
        {
            path = intent.getStringExtra("PATH")
            images = getAllShownImagesPath(path.toString())
        }else {
            Toast.makeText(this , "Please Accept Permission to Perform Task" , Toast.LENGTH_SHORT).show()
            super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        }
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }
}