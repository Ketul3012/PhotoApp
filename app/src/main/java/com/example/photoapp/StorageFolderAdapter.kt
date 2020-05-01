package com.example.photoapp

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import java.io.File

class StorageFolderAdapter(val folders: ArrayList<Folder>?/*, val foldersNames: Array<String>?, val images: Array<String>?*/, val itemClick: (url :String?) -> Unit) :
    RecyclerView.Adapter<StorageFolderAdapter.Storageholder>() {

    inner  class Storageholder constructor( view : View) : RecyclerView.ViewHolder(view) {

        val imageView = view.findViewById<ImageView>(R.id.iv_main)
        val textView = view.findViewById<TextView>(R.id.tv_main)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Storageholder {
        return Storageholder(LayoutInflater.from(parent.context).inflate(R.layout.storage_folder_item , null , false))
    }

    override fun getItemCount(): Int {
        return folders?.size!!
    }

    override fun onBindViewHolder(holder: Storageholder, position: Int) {
        Glide.with(holder.imageView).load(File(Uri.parse(folders?.get(position)?.firstImage).path)).centerCrop().into(holder.imageView)
        holder.textView.text = folders?.get(position)?.name
        holder.imageView.setOnClickListener {
            itemClick.invoke(folders?.get(position)?.path)
        }
    }
}