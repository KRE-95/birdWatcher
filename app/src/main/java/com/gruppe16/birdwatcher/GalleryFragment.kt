package com.gruppe16.birdwatcher

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import com.gruppe16.birdwatcher.adapter.GalleryRecyclerAdapter
import com.gruppe16.birdwatcher.data.Listing
import com.gruppe16.birdwatcher.databinding.FragmentGalleryBinding
import java.text.AttributedCharacterIterator.Attribute.READING


class GalleryFragment : Fragment(R.layout.fragment_gallery) {

    // lage front-end på gallery - done

     // sett opp RecycleView_galley

 // disse binder fragment til .kt
private var _binding : FragmentGalleryBinding? = null
 private val bindingGalleryFragment get() = _binding!!


private lateinit var adapter: GalleryRecyclerAdapter
private lateinit var recyclerView: RecyclerView
//private lateinit var galleryArrayList : ArrayList<Listing>
var db = FirebaseFirestore.getInstance()
 var galleryArrayList = ArrayList<Listing>()

 lateinit var pictureId : Array<Int>

 lateinit var listing: Array<String>

 override fun onCreateView( //denne fungerer, men ingen visning enda

  inflater: LayoutInflater,
  container: ViewGroup?,
  savedInstanceState: Bundle?
 ): View? {
  _binding = FragmentGalleryBinding.inflate(inflater, container, false)

//TODO: Henter data og skriver ut navn på fugler i galleryArrayList - bør kanskje flyttes?
  getDataFromFirestore()



  return bindingGalleryFragment.root
 }

 override fun onCreate(savedInstanceState: Bundle?) {
  super.onCreate(savedInstanceState)


  //recyclerView = requireView().findViewById<RecyclerView>(R.id.RecyclerView)

  //recyclerView.hasFixedSize()
  //recyclerView.layoutManager = LinearLayoutManager(this) //TODO: define context?

 }

 override fun onDestroyView() {
  super.onDestroyView()
  // Benyttes kun for test, sjekker hvor mange fugler som ligger i galleriet når det ødelegges
  println(galleryArrayList.size)
  _binding = null
 }


 fun getDataFromFirestore(){

  db.collection("listings")//TODO Add query to sort by descending?
   .get()
   .addOnCompleteListener { task ->
    if (task.isSuccessful) {
     for (document in task.result) {
      Log.d(TAG, document.id + " => " + document.data)

      //For hvert leste element legges elementet til i lokal lagring
      val newObject = document.toObject(Listing::class.java)
     galleryArrayList.add(newObject)
     }
    } else {
     Log.w(TAG, "Error getting documents.", task.exception)
    }

    }


 }




    // Koble recyleview med items fra layout done

    // lage mappe kalt adapter - done

    // i adapter koble alt info av list item osv


    // hente bilde/text fra data Listing og koble til Firebase?


}