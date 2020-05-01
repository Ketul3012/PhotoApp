package com.example.photoapp

import android.app.WallpaperManager
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.graphics.drawable.toBitmap
import androidx.fragment.app.DialogFragment
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import kotlinx.android.synthetic.main.dialog_image.*
import kotlinx.android.synthetic.main.dialog_image.view.*
import pub.devrel.easypermissions.EasyPermissions
import java.io.FileOutputStream

class ImageFragment : DialogFragment() {

    private val PERMISSIONSWRITE = arrayOf( android.Manifest.permission.READ_EXTERNAL_STORAGE , android.Manifest.permission.WRITE_EXTERNAL_STORAGE)

    companion object
    {
        const val  TAG = "ImageFragment"
        const val ARGS_URL = "ARGS_URL"
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.dialog_image, container , false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val url = arguments?.getString(ARGS_URL)
        view.progress_circular.visibility = View.VISIBLE
        Glide.with(iv_main1)
            .load(url)
            .listener(object  : RequestListener<Drawable>{
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<Drawable>?,
                    isFirstResource: Boolean
                ): Boolean {
                    view.progress_circular.visibility = View.VISIBLE
                    dialog?.dismiss()
                    return false
                }

                override fun onResourceReady(
                    resource: Drawable?,
                    model: Any?,
                    target: Target<Drawable>?,
                    dataSource: DataSource?,
                    isFirstResource: Boolean
                ): Boolean {
                    view.progress_circular.visibility = View.GONE
                    view.btn_set_wp.visibility = View.VISIBLE
                    view.btn_savetoFiles.visibility = View.VISIBLE
                    return false
                }

            })
            .optionalFitCenter()
            .into(iv_main1)

            if (! EasyPermissions.hasPermissions(requireContext() , PERMISSIONSWRITE[0] , PERMISSIONSWRITE[1]))
            {
                EasyPermissions.requestPermissions(this ,"Please Allow Permission" , 10 ,  PERMISSIONSWRITE[0] , PERMISSIONSWRITE[1] )
            }
            btn_savetoFiles.setOnClickListener {
                saveToDownloads()
            }

            btn_set_wp.setOnClickListener {
                setWallpaper()
            }
    }

    override fun getTheme(): Int {
        return R.style.DialogStyle
    }


    private fun  saveToDownloads()
    {
        val bitmap = iv_main1.drawable.toBitmap()
        val file = MainActivity.getOutputDirectory()
        try {
            val outStream = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.JPEG , 100 , outStream)
            outStream.flush()
            outStream.close()
            Toast.makeText(requireContext(), "Image Saved in Downloads" , Toast.LENGTH_SHORT).show()
        }catch (e : Exception)
        {
            Toast.makeText(requireContext() , "Image Not Saved" , Toast.LENGTH_SHORT).show()
            e.printStackTrace()
        }
    }

    private fun setWallpaper()
    {
        val wpManager = WallpaperManager.getInstance(requireContext())
        try {
            wpManager.setBitmap(iv_main1.drawable.toBitmap())
            Toast.makeText(requireContext(), "Image Set as Wallpaper" , Toast.LENGTH_SHORT).show()
        }catch (e : java.lang.Exception)
        {
            Toast.makeText(requireContext(), "Image Not Set as Wallpaper" , Toast.LENGTH_SHORT).show()
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
             saveToDownloads()
        }else {
            Toast.makeText(activity?.baseContext , "Please Accept Permission to Perform Task" , Toast.LENGTH_SHORT).show()
            super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        }
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)

    }
}