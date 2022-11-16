package com.gruppe16.birdwatcher.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.gruppe16.birdwatcher.R
import com.gruppe16.birdwatcher.data.Listing


class GalleryRecyclerAdapter (
    private val galleryListing : ArrayList<Listing>,
    private val onListingClickedListener: OnListingClickedListener
    ): RecyclerView.Adapter<GalleryRecyclerAdapter.ViewHolder>() {
    var currentItem : Listing? = null
    //var onItemClick : ((Listing) -> Unit)? = null;

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val  itemView = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.gallery_list_item, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        currentItem = galleryListing[position]
        Glide.with(holder.itemView).load(galleryListing[position].picture).into(holder.itemView.findViewById(R.id.ImageV_Display_Gallery))
        holder.birdName.text = currentItem?.birdName

        holder.itemView.setOnClickListener {
            onListingClickedListener.onListingClicked(position)
        }

    //TODO: Flere ting legges til her ettersom vi får mer data på plass for listing

    }

    override fun getItemCount(): Int {
       return galleryListing.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
     // gallery_list + listing

        //val picture: ImageView = itemView.findViewById(R.id.ImageV_Display_Gallery)
        val birdName: TextView = itemView.findViewById(R.id.tV_BirdName)

        fun initClick(item: Listing, action: OnListingClickedListener){
            itemView.setOnClickListener{
                action.onListingClicked(bindingAdapterPosition)
            }
        }
    }

    interface OnListingClickedListener {
        fun onListingClicked(position: Int)
    }
}