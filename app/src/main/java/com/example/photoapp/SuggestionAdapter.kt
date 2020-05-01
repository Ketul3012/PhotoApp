package com.example.photoapp

import android.content.Context
import android.widget.ArrayAdapter
import android.widget.Filter
import androidx.annotation.LayoutRes


class SuggestionAdapter<String>(context: Context, @LayoutRes resource: Int, private var items: List<kotlin.String>) : ArrayAdapter<kotlin.String>(context, resource , items) {
    private var filteredItems: List<kotlin.String>? = null
    private var mFilter: ArrayFilter? = null
    private var allItems: List<kotlin.String> = items

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItem(position: Int): kotlin.String {
        return items[position]
    }

    override fun getFilter(): Filter {
        if (mFilter == null) {
            mFilter = ArrayFilter()
        }
        return mFilter!!
    }

    override fun getCount(): Int { return items.size
    }

    inner class ArrayFilter : Filter() {
        override fun performFiltering(prefix: CharSequence?): FilterResults {
            val results = FilterResults()
            val filteredItem = ArrayList<kotlin.String>()
            if (prefix != "")
            {

                for( va in items)
                {
                    if (va.contains(prefix.toString().toLowerCase()) )
                    {
                        filteredItem.add(va)
                    }
                }
            }
            results.values = filteredItem
            results.count = filteredItem.size
            return results
        }

         override fun publishResults(constraint: CharSequence?, results: FilterResults) {
            filteredItems = results.values as ArrayList<kotlin.String>
            if (filteredItems?.size  != 0) {
                items = filteredItems as ArrayList<kotlin.String>
                notifyDataSetChanged()
            } else {
                items=allItems
                notifyDataSetInvalidated()
            }
        }
    }

}