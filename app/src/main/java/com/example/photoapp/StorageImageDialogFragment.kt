package com.example.photoapp

import android.app.WallpaperManager
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

class StorageImageDialogFragment : DialogFragment() {


    companion object
    {
        const val  TAG = "StorageImageDialogFragment"
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
        btn_savetoFiles.text = getString(R.string.set_a_description)
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

            btn_savetoFiles.setOnClickListener {
                val dialogFragment = ImageDescriptionDIalogFragment()
                dialogFragment.arguments = Bundle().apply {
                    putString(ImageDescriptionDIalogFragment.ARGS_VAL , arguments?.getString(ARGS_URL))
                }
                dialogFragment.isCancelable = true
                dialogFragment.show(childFragmentManager , ImageDescriptionDIalogFragment.TAG)
            }

            btn_set_wp.setOnClickListener {
                setWallpaper()
            }
    }

    override fun getTheme(): Int {
        return R.style.DialogStyle
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

}