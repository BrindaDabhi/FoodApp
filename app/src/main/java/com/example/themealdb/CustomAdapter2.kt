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

class CustomAdapter2(private val arrayListdetails2: ArrayList<Model2>, private val listener2: MainActivity2) :

    RecyclerView.Adapter<ViewHolder2>() {

    var arrayFilterList2 = ArrayList<Model2>()

    init {
        arrayFilterList2 = arrayListdetails2
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder2 {

        val view = LayoutInflater.from(parent.context).inflate(R.layout.adapter_layout2, parent, false)

        val viewHolder = ViewHolder2(view)

        view.setOnClickListener{
            listener2.onItemClicked2(arrayFilterList2[viewHolder.adapterPosition])
        }

        return viewHolder
    }

    override fun onBindViewHolder(holder: ViewHolder2, position: Int) {
        val currentItem = arrayFilterList2[position]

        holder.textView.text = currentItem.name2
        holder.textView.isSelected = true
        Picasso.get().load(currentItem.img2).into(holder.image)
    }

    override fun getItemCount(): Int {
        return arrayFilterList2.size
    }

    fun getFilter2(): Filter {

        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {

                val charSearch = constraint.toString()
                arrayFilterList2 = if (charSearch.isEmpty()) {
                    arrayListdetails2
                } else {
                    val resultList = ArrayList<Model2>()
                    for (row in arrayListdetails2) {
                        if (row.name2.lowercase(Locale.ROOT).contains(charSearch.lowercase(Locale.ROOT))) {
                            resultList.add(row)
                        }
                    }
                    resultList
                }
                val filterResults = FilterResults()
                filterResults.values = arrayFilterList2
                return filterResults

            }

            @Suppress("UNCHECKED_CAST")
            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                arrayFilterList2 = results?.values as ArrayList<Model2>
                notifyDataSetChanged()
            }

        }

    }

}

class ViewHolder2(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val image: ImageView = itemView.findViewById(R.id.foodImg2)
    val textView: TextView = itemView.findViewById(R.id.foodName2)
}

interface ItemClicked2 {
    fun onItemClicked2(item: Model2)
}