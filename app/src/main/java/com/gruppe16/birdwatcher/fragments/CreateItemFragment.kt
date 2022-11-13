package com.gruppe16.birdwatcher.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.fragment.app.activityViewModels
import com.gruppe16.birdwatcher.R
import com.gruppe16.birdwatcher.components.FirebaseDatabase
import com.gruppe16.birdwatcher.data.Listing
import com.gruppe16.birdwatcher.databinding.FragmentCreateItemBinding
import com.gruppe16.birdwatcher.viewmodels.HomeCreateViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.util.UUID


class CreateItemFragment : Fragment() {

    private val viewModel: HomeCreateViewModel by activityViewModels()
    private var _binding: FragmentCreateItemBinding? = null
    private val binding get() = _binding!!
    private lateinit var db : FirebaseDatabase


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentCreateItemBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        db = FirebaseDatabase()
        super.onViewCreated(view, savedInstanceState)
        val pictureUri = viewModel.pictureUri.value?.toUri()
        val userName = binding.etUser.editText?.text
        val description = binding.etDescription.editText?.text
        val date = viewModel.date.value
        binding.imageView2.setImageURI(pictureUri)
        binding.editTextDate.setText(date)
        binding.button.setOnClickListener {
            val pictureId = UUID.randomUUID().toString()
            db.uploadPhotoToFirebase(pictureId, pictureUri)
        }
        binding.saveListing.setOnClickListener {

            val listingToSave = Listing("TODO", description.toString(), db.pictureUrl!!, date.toString(), userName.toString())
            Log.d("CREATE", "LISTING: ${listingToSave.picture} og ${listingToSave.description}")
            findNavController().navigate(R.id.action_createItem_to_homeFragment)
        }
    }
}
