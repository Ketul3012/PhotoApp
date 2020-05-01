package com.example.photoapp

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.dialog_discription.*
import kotlinx.android.synthetic.main.dialog_discription.view.*


class ImageDescriptionDIalogFragment : DialogFragment() {


    companion object
    {
        const val  TAG = "ImageDescriptionDIalogFragment"
        const val DESCRIPTION = "Desc"
        const val ARGS_VAL = "Args"

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.dialog_discription, container , false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var map : HashMap<String , String> = try {
            loadMap()
        }catch (e : Exception)
        {
            HashMap()
        }
        val url = arguments?.getString(ARGS_VAL)
        view.btn_cancel.setOnClickListener {
            dismiss()
        }

        view.btn_submit.setOnClickListener {
            map[ed_discription.text.toString().toLowerCase()] = url.toString()
            setMap(map)
            Toast.makeText(requireContext() , "Description Assigned" , Toast.LENGTH_SHORT).show()
            dismiss()
        }

    }

    override fun getTheme(): Int {
        return R.style.DialogStyle
    }

    private fun loadMap() : HashMap<String , String>
    {
        val sharedPreferences = activity?.getSharedPreferences(getString(R.string.app_name) , Context.MODE_PRIVATE)
        var map  = ""
        val type = object : TypeToken<HashMap<String, String>>() {}.type

        if (sharedPreferences != null)
        {
            map = sharedPreferences.getString(DESCRIPTION , "").toString()
        }
        return if (map.isEmpty())
        {
            HashMap()
        }else {
            Gson().fromJson(map, type)
        }

    }


    private fun setMap(map: HashMap<String, String>)
    {
        val sharedPreferences = activity?.getSharedPreferences(getString(R.string.app_name) , Context.MODE_PRIVATE)
        if (sharedPreferences != null)
        {
            val gson  = Gson().toJson(map)
            sharedPreferences.edit().putString(DESCRIPTION , gson).apply()
        }
    }


}