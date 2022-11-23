package com.gruppe16.birdwatcher.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.gruppe16.birdwatcher.R
import com.gruppe16.birdwatcher.data.ListingDAO


class GalleryRecyclerAdapter (

    private val onListingClickedListener: OnListingClickedListener
    ): RecyclerView.Adapter<GalleryRecyclerAdapter.ViewHolder>(), Filterable {
    private var currentItem : ListingDAO? = null

    private lateinit var galleryArrayList : ArrayList<ListingDAO>
    private lateinit var galleryFilterList: ArrayList<ListingDAO>

    fun setData(galleryArrayList: ArrayList<ListingDAO>) {
        this.galleryArrayList = galleryArrayList
        this.galleryFilterList = galleryArrayList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val  itemView = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.gallery_list_item, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        currentItem = galleryArrayList[position]
        Glide.with(holder.itemView).load(galleryArrayList[position].picture).into(holder.itemView.findViewById(R.id.ImageV_Display_Gallery))
        holder.birdName.text = currentItem?.birdName
        holder.userName.text = currentItem?.user

        holder.itemView.setOnClickListener {
            onListingClickedListener.onListingClicked(position)
        }

    //TODO: Flere ting legges til her ettersom vi får mer data på plass for listing

    }

    override fun getItemCount(): Int {
       return galleryArrayList.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val birdName: TextView = itemView.findViewById(R.id.tV_BirdName)
        val userName: TextView = itemView.findViewById(R.id.tV_user)
    }

    interface OnListingClickedListener {
        fun onListingClicked(position: Int)
    }

    override fun getFilter(): Filter {
        return object: Filter() {
            val result = FilterResults()
            val filteredList = ArrayList<ListingDAO>()
            override fun performFiltering(constraint: CharSequence?): FilterResults {

                if(constraint.isNullOrEmpty()) {
                    result.count = galleryFilterList.size
                    result.values = galleryFilterList
                } else {
                    val searchWord = constraint.toString()

                    for(listing in galleryFilterList) {
                        if(
                            listing.birdName.contains(searchWord, ignoreCase = true) ||
                            listing.user.contains(searchWord, ignoreCase = true)
                        )
                            filteredList.add(listing)
                    }
                    result.count = filteredList.size
                    result.values = filteredList
                }
                return result
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                galleryArrayList = result.values as ArrayList<ListingDAO>
                notifyDataSetChanged()
            }
        }
    }
}