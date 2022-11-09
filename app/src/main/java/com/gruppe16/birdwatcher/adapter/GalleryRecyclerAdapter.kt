package com.gruppe16.birdwatcher.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.gruppe16.birdwatcher.R
import com.gruppe16.birdwatcher.data.Listing

class GalleryRecyclerAdapter (private val galleryListing : ArrayList<Listing> ): RecyclerView.Adapter<GalleryRecyclerAdapter.ViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val  itemView = LayoutInflater.from(parent.context).inflate(R.layout.gallery_list_item, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = galleryListing[position]

      //  holder.picture.text = currentItem.picture
        holder.birdName.text = currentItem.birdName
        holder.description.text = currentItem.description

    //TODO: Flere ting legges til her ettersom vi får mer data på plass for listing

    }

    override fun getItemCount(): Int {
       return galleryListing.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
     // gallery_list + listing

        //val picture: ShapeableImageView = itemView.findViewById(R.id.ImageV_Display_Gallery) //TODO: see if you can rework the view to have this as a textview and just use string?
        val birdName: TextView = itemView.findViewById(R.id.tV_BirdName)
        val description: TextView = itemView.findViewById(R.id.tV_Description_Gallery)

        //TODO: Flere ting legges til her ettersom vi får mer data på plass for listing

    }

}