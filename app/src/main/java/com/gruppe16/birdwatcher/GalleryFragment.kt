package com.gruppe16.birdwatcher

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import com.gruppe16.birdwatcher.adapter.GalleryRecyclerAdapter
import com.gruppe16.birdwatcher.data.Listing
import com.gruppe16.birdwatcher.databinding.FragmentGalleryBinding


class GalleryFragment : Fragment(R.layout.fragment_gallery) {

 // disse binder fragment til .kt
private var _binding : FragmentGalleryBinding? = null
private val bindingGalleryFragment get() = _binding!!


private lateinit var ourAdapter: GalleryRecyclerAdapter
private lateinit var recyclerView: RecyclerView
private lateinit var galleryArrayList : ArrayList<Listing>
var db = FirebaseFirestore.getInstance()


 //TODO: Disse to variablene er allerede i en listing.
 lateinit var picture : Array<String>
 lateinit var birdName: Array<String>
 //lateinit var description: Array<String>

 override fun onCreate(savedInstanceState: Bundle?) {
  super.onCreate(savedInstanceState)


// recyclerView = requireView().findViewById(R.id.RecyclerView)
  //recyclerView.layoutManager = LinearLayoutManager(this)
 // recyclerView.hasFixedSize()
  //recyclerView.layoutManager = LinearLayoutManager(this) //TODO: define context?

  //galleryArrayList = arrayListOf()

 // adapter = GalleryRecyclerAdapter(galleryArrayList)

 // recyclerView.adapter = adapter
 }


 //denne fungerer, men ingen visning enda //TODO: Denne het onCreateView før
 override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
super.onViewCreated(view, savedInstanceState)
  dataInitialize()
/*  inflater: LayoutInflater,
  container: ViewGroup?,
  savedInstanceState: Bundle?
 ): View? {
  _binding = FragmentGalleryBinding.inflate(inflater, container, false)*/

  //galleryArrayList = arrayListOf()
//TODO: Henter data og skriver ut navn på fugler i galleryArrayList - bør kanskje flyttes?
 // getDataFromFirestore()

val layoutManager = LinearLayoutManager(context)
  recyclerView = view.findViewById(R.id.RecyclerView)
  recyclerView.layoutManager = layoutManager
 recyclerView.hasFixedSize()
  ourAdapter = GalleryRecyclerAdapter(galleryArrayList)
 recyclerView.adapter = ourAdapter

  //return bindingGalleryFragment.root
 }



 override fun onDestroyView() {
  super.onDestroyView()
  //TODO: Benyttes kun for test, sjekker hvor mange fugler som ligger i galleriet når det ødelegges
  println(galleryArrayList.size)
  _binding = null
 }


 private fun dataInitialize(){

  galleryArrayList = arrayListOf<Listing>()

  picture = arrayOf(
   R.drawable.fossekallmain
    .toString())

  birdName = arrayOf(
   getString(R.string.birdName_1)
  )



  for (i in picture.indices){

   val listing = Listing(birdName[i]) //picture[i]
   galleryArrayList.add(listing)

  }

 }


/* fun getDataFromFirestore(){

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
   //  ourAdapter.notifyDataSetChanged()
    } else {
     Log.w(TAG, "Error getting documents.", task.exception)
    }

    }


 }*/

//TODO: JUST NOTES
    // lage front-end på gallery - done

    // Koble recyleview med items fra layout done

    // lage mappe kalt adapter - done

    // i adapter koble alt info av list item osv
 // sett opp RecycleView_galley

    // hente bilde/text fra data Listing og koble til Firebase?


}