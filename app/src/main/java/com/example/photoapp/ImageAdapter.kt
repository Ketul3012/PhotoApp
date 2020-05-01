package com.example.photoapp

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class ImageAdapter(val context: Context, private val itemClick: (url : String) -> Unit) : PagedListAdapter< ImageResponse.ImageResponseItem.Urls , ImageAdapter.ViewHolder>(DIFF_UTILS) {

    companion object
    {
        private val DIFF_UTILS = object : DiffUtil.ItemCallback<ImageResponse.ImageResponseItem.Urls>() {
            override fun areItemsTheSame(oldItem: ImageResponse.ImageResponseItem.Urls, newItem: ImageResponse.ImageResponseItem.Urls): Boolean {
                return oldItem.thumb == newItem.thumb
            }

            override fun areContentsTheSame(oldItem: ImageResponse.ImageResponseItem.Urls, newItem: ImageResponse.ImageResponseItem.Urls): Boolean {
                return oldItem == newItem
            }

        }
    }


    class ViewHolder(v : View) : RecyclerView.ViewHolder(v) {
        val imageView = v.findViewById<ImageView>(R.id.iv_main)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return  ViewHolder(LayoutInflater.from(context).inflate(R.layout.image_item , null , false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Glide.with(holder.imageView).load(getItem(position)?.thumb).fitCenter().into(holder.imageView)
        holder.imageView.setOnClickListener {
            itemClick.invoke(getItem(position)?.full.toString())
        }
    }
}
