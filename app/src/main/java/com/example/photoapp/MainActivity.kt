package com.example.photoapp

import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Environment
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import pub.devrel.easypermissions.EasyPermissions
import java.io.File
import kotlin.random.Random


class MainActivity : AppCompatActivity() {

    companion object {

        fun getOutputDirectory(): File {
            val string = Random(10000).nextInt().toString()
            val file = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).path, "Image${string}.jpg")
            return file
        }
    }
    private val PERMISSIONSWRITE = arrayOf( android.Manifest.permission.READ_EXTERNAL_STORAGE , android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        if (! EasyPermissions.hasPermissions(applicationContext , PERMISSIONSWRITE[0] , PERMISSIONSWRITE[1]))
        {
            EasyPermissions.requestPermissions(this ,"Please Allow Permission" , 10 ,  PERMISSIONSWRITE[0] , PERMISSIONSWRITE[1] )
        }
        vp_tab.adapter = ViewPagerAdapter(supportFragmentManager)
        tab_layout_main.setupWithViewPager(vp_tab)
        tab_layout_main.getTabAt(0)?.text = "Photos"
        tab_layout_main.getTabAt(1)?.text = "Explore"
        tab_layout_main.getTabAt(2)?.text = "Settings"

    }

    override fun onBackPressed() {
        moveTaskToBack(true)
    }
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (grantResults[0] == PackageManager.PERMISSION_DENIED && requestCode==10)
        {
            Toast.makeText(this , "Please Enable Permission for better performance" , Toast.LENGTH_LONG).show()
        }else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        }
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }

}
