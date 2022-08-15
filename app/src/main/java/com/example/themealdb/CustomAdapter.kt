package com.example.themealdb

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import java.util.*
import kotlin.collections.ArrayList

class CustomAdapter(private val arrayListdetails: ArrayList<Model1>, private val listener: MainActivity) :

    RecyclerView.Adapter<ViewHolder>() {

    var arrayFilterList = ArrayList<Model1>()

    init {
        arrayFilterList = arrayListdetails
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val view = LayoutInflater.from(parent.context).inflate(R.layout.adapter_layout, parent, false)

        val viewHolder = ViewHolder(view)

        view.setOnClickListener{
            listener.onItemClicked(arrayFilterList[viewHolder.adapterPosition])
        }

        return viewHolder
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = arrayFilterList[position]

        holder.textView.text = currentItem.name
        Picasso.get().load(currentItem.img).into(holder.image)
    }

    override fun getItemCount(): Int {
        return arrayFilterList.size
    }

    fun getFilter(): Filter {

        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {

                val charSearch = constraint.toString()
                arrayFilterList = if (charSearch.isEmpty()) {
                    arrayListdetails
                } else {
                    val resultList = ArrayList<Model1>()
                    for (row in arrayListdetails) {
                        if (row.name.lowercase(Locale.ROOT).contains(charSearch.lowercase(Locale.ROOT))) {
                            resultList.add(row)
                        }
                    }
                    resultList
                }
                val filterResults = FilterResults()
                filterResults.values = arrayFilterList
                return filterResults

            }

            @Suppress("UNCHECKED_CAST")
            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                arrayFilterList = results?.values as ArrayList<Model1>
                notifyDataSetChanged()
            }

        }

    }

}

class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val image: ImageView = itemView.findViewById(R.id.foodImg)
    val textView: TextView = itemView.findViewById(R.id.foodName)
}

interface ItemClicked {
    fun onItemClicked(item: Model1)
}
