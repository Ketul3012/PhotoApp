package com.example.photoapp

import ImageViewModel
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import kotlinx.android.synthetic.main.fragment_main.*

class MainImageFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_main , container , false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        rv_main.setHasFixedSize(true)
        rv_main.setItemViewCacheSize(20)
        val imageViewModel = ViewModelProvider(this).get(ImageViewModel::class.java)
        val adapter = ImageAdapter( requireActivity()){
            val dialogFragment = ImageFragment()
            dialogFragment.arguments = Bundle().apply {
                putString(ImageFragment.ARGS_URL,it)
            }
            dialogFragment.isCancelable = true
            dialogFragment.show(childFragmentManager, ImageFragment.TAG)
        }
        imageViewModel.itemPagedList?.observe(requireActivity() , Observer {
            adapter.submitList(it)
        })
        rv_main.adapter = adapter
        val layoutManager = StaggeredGridLayoutManager(3 , StaggeredGridLayoutManager.VERTICAL)
        layoutManager.gapStrategy = StaggeredGridLayoutManager.GAP_HANDLING_MOVE_ITEMS_BETWEEN_SPANS
        rv_main.layoutManager =  layoutManager

    }
}