package com.gruppe16.birdwatcher

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.gruppe16.birdwatcher.adapter.GalleryRecyclerAdapter
import com.gruppe16.birdwatcher.data.Listing

class GalleryFragment : Fragment(R.layout.fragment_gallery) {



    // lage front-end på gallery - done

     // sett opp RecycleView_galley 

private lateinit var adapter: GalleryRecyclerAdapter
private lateinit var recyclerView: RecyclerView
private lateinit var galleryArrayList : ArrayList<Listing>

 lateinit var pictureId : Array<Int>

 lateinit var listing: Array<String>







    // Koble recyleview med items fra layout done

    // lage mappe kalt adapter - done

    // i adapter koble alt info av list item osv


    // hente bilde/text fra data Listing og koble til Firebase?


}