package com.gruppe16.birdwatcher.fragments

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import com.gruppe16.birdwatcher.R
import com.gruppe16.birdwatcher.adapter.GalleryRecyclerAdapter
import com.gruppe16.birdwatcher.data.ListingDAO
import com.gruppe16.birdwatcher.databinding.FragmentGalleryBinding
import com.gruppe16.birdwatcher.viewmodels.SharedViewModel


class GalleryFragment : Fragment(), GalleryRecyclerAdapter.OnListingClickedListener {

    private val viewModel: SharedViewModel by activityViewModels()
    private var _binding : FragmentGalleryBinding? = null
    private val binding get() = _binding!!
    private lateinit var ourAdapter: GalleryRecyclerAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var galleryArrayList : ArrayList<ListingDAO>
    private lateinit var db : FirebaseFirestore

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentGalleryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        galleryArrayList = arrayListOf()
        getDataFromFirestore()

        val layoutManager = LinearLayoutManager(context)
        recyclerView = view.findViewById(R.id.RecyclerView)
        recyclerView.layoutManager = layoutManager
        recyclerView.hasFixedSize()
        ourAdapter = GalleryRecyclerAdapter(this)
        recyclerView.adapter = ourAdapter
        ourAdapter.setData(galleryArrayList)

        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                ourAdapter.filter.filter(query)
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                ourAdapter.filter.filter(newText)
                return true
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }

    private fun getDataFromFirestore() {
        db = FirebaseFirestore.getInstance()
        db.collection("listings")
            .get()
            .addOnCompleteListener { task ->
             if (task.isSuccessful) {
                 for (document in task.result) {
                    Log.d(TAG, document.id + " => " + document.data)

                    //For hvert leste objekt legges det til i lokal lagring
                    val newObject = document.toObject(ListingDAO::class.java)
                     newObject.listingId = document.id
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
        viewModel.setPlace(clicked.place)
        viewModel.setUser(clicked.user)
        viewModel.setDate(clicked.date)
        viewModel.setListId(clicked.listingId)
        findNavController().navigate(R.id.action_galleryFragment_to_selectedItemFragment)
    }
}