package com.gruppe16.birdwatcher
import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import com.gruppe16.birdwatcher.adapter.GalleryRecyclerAdapter
import com.gruppe16.birdwatcher.data.Listing
import com.gruppe16.birdwatcher.databinding.FragmentGalleryBinding
import com.gruppe16.birdwatcher.viewmodels.SharedViewModel

class GalleryFragment : Fragment(R.layout.fragment_gallery), GalleryRecyclerAdapter.OnListingClickedListener {
 private val viewModel: SharedViewModel by activityViewModels()
 private var _binding : FragmentGalleryBinding? = null
 private val binding get() = _binding!!
 private lateinit var ourAdapter: GalleryRecyclerAdapter
 private lateinit var recyclerView: RecyclerView
 private lateinit var galleryArrayList : ArrayList<Listing>
 private lateinit var db : FirebaseFirestore

 override fun onCreateView(
  inflater: LayoutInflater, container: ViewGroup?,
  savedInstanceState: Bundle?
 ): View? {
  _binding = FragmentGalleryBinding.inflate(inflater, container, false)
  return binding.root
 }

 //denne fungerer, men ingen visning enda
 override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
  super.onViewCreated(view, savedInstanceState)
  galleryArrayList = arrayListOf()
  getDataFromFirestore()

  val layoutManager = LinearLayoutManager(context)
  recyclerView = view.findViewById(R.id.RecyclerView)
  recyclerView.layoutManager = layoutManager
  recyclerView.hasFixedSize()
  ourAdapter = GalleryRecyclerAdapter(galleryArrayList, this)
  recyclerView.adapter = ourAdapter
 }

 override fun onDestroyView() {
  super.onDestroyView()
  //TODO: Benyttes kun for test, sjekker hvor mange fugler som ligger i galleriet når det ødelegges
  println(galleryArrayList.size)
 }

private fun getDataFromFirestore(){

 db = FirebaseFirestore.getInstance()
 db.collection("listings")//TODO Add query to sort by descending?
  .get()
  .addOnCompleteListener { task ->
   if (task.isSuccessful) {
    for (document in task.result) {
     Log.d(TAG, document.id + " => " + document.data)

     //For hvert leste objekt legges det til i lokal lagring
     val newObject = document.toObject(Listing::class.java)
     galleryArrayList.add(newObject)
    }
    ourAdapter.notifyDataSetChanged()
   } else {
    Log.w(TAG, "Error getting documents.", task.exception)
   }
  }
}

 override fun onListingClicked(position: Int) {
  val clicked = galleryArrayList[position]
  viewModel.setBirdName(clicked.birdName)
  viewModel.setDescription(clicked.description)
  viewModel.setPicture(clicked.picture)
  viewModel.setUser(clicked.user)
  viewModel.setDate(clicked.date)
  findNavController().navigate(R.id.action_galleryFragment_to_selectedItemFragment)
 }


}

//TODO: JUST NOTES
// Få tak på data som ligger lokalt etter databasekall og føre det til recyclerView





