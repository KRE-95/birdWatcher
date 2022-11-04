package com.gruppe16.birdwatcher.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.imageview.ShapeableImageView
import com.gruppe16.birdwatcher.R
import com.gruppe16.birdwatcher.data.Listing


class GalleryRecyclerAdapter (private val galleryListing : ArrayList<Listing> ): RecyclerView.Adapter<GalleryRecyclerAdapter.ViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val  itemView = LayoutInflater.from(parent.context).inflate(R.layout.gallery_list_item, parent, false)
        return ViewHolder(itemView)

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem= galleryListing[position]
        holder.birdName.text = currentItem.birdName
        holder.description.text = currentItem.description
       // holder.place.text = currentItem.place
       // holder.time.text = currentItem.time
        //holder.picture.setImageResource(currentItem.picture)

    }

    override fun getItemCount(): Int {
       return galleryListing.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
     // gallery_list + listing
        val birdName: TextView = itemView.findViewById(R.id.tV_BirdName)
        //val picture: ImageView = itemView.findViewById(R.id.ImageV_Display_Gallery)
        val description: TextView = itemView.findViewById(R.id.tV_Description_Gallery)
        val place: TextView = itemView.findViewById(R.id.tV_Place_Gallery)
        val time: TextView = itemView.findViewById(R.id.tV_Time_Gallery)


    }

}