package com.example.photoapp

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Handler
import android.provider.MediaStore
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import pub.devrel.easypermissions.EasyPermissions


class SplashActivity : AppCompatActivity() {
    var foldersPaths = java.util.ArrayList<String>()
    var folders = ArrayList<Folder>()
    private val PERMISSIONSWRITE = arrayOf( android.Manifest.permission.READ_EXTERNAL_STORAGE
                                , android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        if (!EasyPermissions.hasPermissions(this , PERMISSIONSWRITE[0] , PERMISSIONSWRITE[1] ))
        {
            EasyPermissions.requestPermissions(this ,"Please Allow Permission" , 10 ,  PERMISSIONSWRITE[0] , PERMISSIONSWRITE[1])
        }else
        {
            setFolderPaths()
            Handler().postDelayed({
                startActivity(Intent(this@SplashActivity, MainActivity::class.java).putParcelableArrayListExtra("Folders" , folders))
                finish()
            }, 2000)
        }
    }

    /*private fun getAllShownImagesPath(activity: Activity): ArrayList<String?>? {
        val cursor: Cursor?
        val column_index_data: Int
        val listOfAllImages = ArrayList<String?>()
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
        column_index_data = cursor!!.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
        //Log.e("Bucket Name" , column_index_folder_name)

        while (cursor.moveToNext()) {
            absolutePathOfImage = cursor.getString(column_index_data)
            listOfAllImages.add(absolutePathOfImage)
        }
        return listOfAllImages
    }*/


    fun setFolderPaths()
    {
        val allImagesuri =
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        val projection = arrayOf(
            MediaStore.Images.ImageColumns.DATA, MediaStore.Images.Media.DISPLAY_NAME,
            MediaStore.Images.Media.BUCKET_DISPLAY_NAME, MediaStore.Images.Media.BUCKET_ID
        )
        val cursor =
            this.contentResolver.query(allImagesuri, projection, null, null, null)
        try {
            cursor?.moveToFirst()
            do {
                val folder =
                    cursor!!.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_DISPLAY_NAME))
                val datapath =
                    cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA))
                var folderpath =
                    datapath.substring(0, datapath.lastIndexOf("$folder/"))
                folderpath= "$folderpath$folder/"

                if (!foldersPaths.contains(folderpath)) {
                    foldersPaths.add(folderpath)
                    folders.add(Folder(folder , folderpath ,datapath))
                }
            } while (cursor!!.moveToNext())
            cursor.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {

        if (grantResults[0]  == PackageManager.PERMISSION_GRANTED && requestCode == 10)
        {
            /*images = getAllShownImagesPath(this)
            if (images?.size != 0) {
                val sharedPreferences =
                    this.getSharedPreferences("PhotoApp", Context.MODE_PRIVATE).edit()
                sharedPreferences.putStringSet("IMAGES", images?.toSet()).apply()
            }*/
            setFolderPaths()
            Handler().postDelayed({
                startActivity(Intent(this@SplashActivity, MainActivity::class.java)
                    .putParcelableArrayListExtra("Folders" , folders))
                finish()
            }, 2000)
        }else {
            Toast.makeText(this , "Please Accept Permission to Perform Task" , Toast.LENGTH_SHORT).show()
            finish()
            super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        }
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }
}
