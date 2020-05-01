package com.example.photoapp

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import kotlinx.android.synthetic.main.fragment_main.*
import pub.devrel.easypermissions.EasyPermissions


class StorageFolderFragment : Fragment() {
    private val PERMISSIONSWRITE = arrayOf( android.Manifest.permission.READ_EXTERNAL_STORAGE , android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
    private  var folders : ArrayList<Folder>? = ArrayList()
    /*private  var foldersNames : Array<String>? = arrayOf()
    private  var firstImages : Array<String>? = arrayOf()*/

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_main , container , false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val sharedPreferences = requireActivity().getSharedPreferences("PhotoApp" , Context.MODE_PRIVATE)
        /*folders = sharedPreferences.getStringSet("FOLDERS" , null)?.toTypedArray()
        foldersNames = sharedPreferences.getStringSet("FOLDERSNAMES" , null)?.toTypedArray()
        firstImages = sharedPreferences.getStringSet("FIRSTIMAGES" , null)?.toTypedArray()*/
        rv_main.setHasFixedSize(true)
        rv_main.setItemViewCacheSize(20)
        //val list = getAllShownImagesPath(requireActivity())
        folders = requireActivity().intent.getParcelableArrayListExtra("Folders")
        rv_main.layoutManager = GridLayoutManager(requireContext() , 2)
        rv_main.adapter = StorageFolderAdapter(folders) {
            val intent = Intent(requireContext() , StorageImageActivity::class.java)
            intent.putExtra("PATH" , it)
            startActivity(intent)
        }
    }

    override fun onResume() {
        if ( ! EasyPermissions.hasPermissions(requireContext() , PERMISSIONSWRITE[0],PERMISSIONSWRITE[1]))
        {
            EasyPermissions.requestPermissions(this ,"Please Allow Permission" , 10 ,  PERMISSIONSWRITE[0] , PERMISSIONSWRITE[1] )
        }
        super.onResume()
    }

    /*private fun getAllShownImagesPath(activity: Activity): ArrayList<String>? {
        val cursor: Cursor?
        val column_index_data: Int
        val column_index_folder_name: String
        val listOfAllImages : ArrayList<String>? = ArrayList()
        var absolutePathOfImage: String? = null
        val uri: Uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        val projection = arrayOf(
            MediaStore.Images.ImageColumns.DATA, MediaStore.Images.Media.DISPLAY_NAME,
            MediaStore.Images.Media.BUCKET_DISPLAY_NAME, MediaStore.Images.Media.BUCKET_ID
        )
        cursor = activity.contentResolver.query(
            uri, projection, null,
            null, null
        )
        column_index_data = cursor!!.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA)
        column_index_folder_name = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_DISPLAY_NAME))
        Log.e("Bucket Name" , column_index_folder_name)
        while (cursor.moveToNext()) {
            absolutePathOfImage = cursor.getString(column_index_data)
            listOfAllImages?.add(absolutePathOfImage)
        }
        return listOfAllImages
    }*/
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {

        if (!(grantResults[0]  == PackageManager.PERMISSION_GRANTED && requestCode == 10))
        {
            Toast.makeText(activity?.baseContext , "Please Accept Permission to Perform Task" , Toast.LENGTH_SHORT).show()
            super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        }
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }

}