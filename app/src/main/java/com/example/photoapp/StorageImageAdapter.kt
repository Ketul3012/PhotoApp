package com.example.photoapp

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import java.io.File

class StorageImageAdapter(val itemClick : (url :String?) -> Unit) :
    RecyclerView.Adapter<StorageImageAdapter.Storageholder>() {
    var list: Array<String>? = null

    inner  class Storageholder constructor( view : View) : RecyclerView.ViewHolder(view) {

        val imageView = view.findViewById<ImageView>(R.id.iv_main)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Storageholder {
        return Storageholder(LayoutInflater.from(parent.context).inflate(R.layout.storage_image_item , null , false))
    }

    override fun getItemCount(): Int {
        return list?.size!!
    }

    override fun onBindViewHolder(holder: Storageholder, position: Int) {
        Glide.with(holder.imageView).load(File(Uri.parse(list?.get(position)).path)).centerCrop().into(holder.imageView)
        holder.imageView.setOnClickListener {
            itemClick.invoke(list?.get(position))
        }
    }

    fun submitData( data : Array<String>?)
    {
        this.list = data
        notifyDataSetChanged()
    }
}