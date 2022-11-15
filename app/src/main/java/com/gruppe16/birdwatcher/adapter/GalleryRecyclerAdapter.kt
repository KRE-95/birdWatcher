package com.gruppe16.birdwatcher.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.gruppe16.birdwatcher.R
import com.gruppe16.birdwatcher.data.Listing
import androidx.navigation.fragment.findNavController


class GalleryRecyclerAdapter (

    private val galleryListing : ArrayList<Listing>
    ): RecyclerView.Adapter<GalleryRecyclerAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val  itemView = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.gallery_list_item, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem : Listing = galleryListing[position]

        Glide.with(holder.itemView).load(galleryListing[position].picture).into(holder.itemView.findViewById(R.id.ImageV_Display_Gallery))
        holder.birdName.text = currentItem.birdName
        holder.user.text=currentItem.user
        //holder.description.text = currentItem.description

        // Navigasjon fra galleri til valgt oppføring
        holder.itemView.setOnClickListener{
            //TODO: FOR TESTING
            println("******************************************************")
            println("NAVIGATE TO FRAGMENT HERE?")
            println("******************************************************")
        }

    //TODO: Flere ting legges til her ettersom vi får mer data på plass for listing

    }

    override fun getItemCount(): Int {
       return galleryListing.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
     // gallery_list + listing

        //val picture: ImageView = itemView.findViewById(R.id.ImageV_Display_Gallery) //TODO: This is not needed.
        val birdName: TextView = itemView.findViewById(R.id.tV_BirdName)
       // val description: TextView = itemView.findViewById(R.id.tV_Description_Gallery)
        val user: TextView = itemView.findViewById(R.id.tV_TakenBy)

        //TODO: Flere ting legges til her ettersom vi får mer data på plass for listing

    }

}