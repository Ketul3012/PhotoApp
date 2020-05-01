package com.example.photoapp

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.fragment_settings.*
import kotlinx.android.synthetic.main.fragment_settings.view.*
import java.io.File

class SettingsFragment : DialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_settings, container , false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        rv_descriptions.layoutManager = LinearLayoutManager(requireContext())
        val map = try {
            val mapString = activity?.getSharedPreferences(getString(R.string.app_name)  , Context.MODE_PRIVATE)?.getString(ImageDescriptionDIalogFragment.DESCRIPTION , "").toString()
            val type = object : TypeToken<HashMap<String, String>>() {}.type
            Gson().fromJson<HashMap<String, String>>(mapString , type)
        }catch (e : Exception) {
            HashMap<String, String>()
        }
        var keys = listOf<String>()
        var urls = listOf<String>()

        try {
            keys = map.keys.toList()
            urls = map.values.toList()
        }catch (e : Exception){
            Log.e("Exception" , e.message)
        }
        rv_descriptions.adapter = DescriptionAdapter( keys, urls)
        view.tv_showDescriptios.setOnClickListener {
            if(rv_descriptions.isVisible)
            {
                rv_descriptions.visibility = View.GONE
            }else
            {
                rv_descriptions.visibility = View.VISIBLE
            }
        }
    }
}

class DescriptionAdapter(val des : List<String>, val url: List<String> ) : RecyclerView.Adapter<DescriptionAdapter.ViewHolder>() {
    inner class ViewHolder constructor (v : View) : RecyclerView.ViewHolder(v) {
        val imageView = v.findViewById<ImageView>(R.id.iv_main)
        val textView = v.findViewById<TextView>(R.id.tv_description)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DescriptionAdapter.ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.description_item , null , false))

    }

    override fun getItemCount(): Int {
        return des.size
    }

    override fun onBindViewHolder(holder: DescriptionAdapter.ViewHolder, position: Int) {
        Glide.with(holder.imageView).load(File(Uri.parse(url[position]).path)).centerCrop().into(holder.imageView)
        holder.textView.text = des[position]
    }



}
