@file:Suppress("DEPRECATION")

package com.gruppe16.birdwatcher.fragments

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.gruppe16.birdwatcher.R
import com.gruppe16.birdwatcher.components.FirebaseDatabase
import com.gruppe16.birdwatcher.data.Listing
import com.gruppe16.birdwatcher.databinding.FragmentCreateItemBinding
import com.gruppe16.birdwatcher.viewmodels.SharedViewModel
import java.util.*


class CreateItemFragment : Fragment() {

    private val viewModel: SharedViewModel by activityViewModels()
    private var _binding: FragmentCreateItemBinding? = null
    private val binding get() = _binding!!
    private lateinit var db: FirebaseDatabase
    private var pictureUri: Uri? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentCreateItemBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        db = FirebaseDatabase()
        super.onViewCreated(view, savedInstanceState)
        pictureUri = viewModel.pictureUri.value?.toUri()
        val date = viewModel.date.value.toString()
        val pictureId = UUID.randomUUID().toString()
        binding.imageView2.setImageURI(pictureUri)
        binding.editTextDate.text = date

        binding.keepPictureBtn.setOnClickListener {
            confirmPicture(pictureId)
        }

        binding.changePictureBtn.setOnClickListener {
            toGallery()
        }

        binding.cancelListing.setOnClickListener {
            cancel(pictureId)
        }

        binding.saveListing.setOnClickListener {
            checkListing(date)
        }
    }

    private fun confirmPicture(pictureId: String) {
        if(pictureUri.toString().isNotEmpty()) {
            db.uploadPhotoToStorage(pictureId, pictureUri)
            binding.saveListing.isEnabled = true
        }
    }

    private fun toGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/Pictures/CameraX-Image"
        startActivityForResult(intent, 0)
    }

    private fun cancel(pictureId: String) {
        if (db.pictureUrl !== null) {
            db.deletePhotoFromStorage(pictureId)
        }
        findNavController().navigate(R.id.action_createItem_to_homeFragment)
    }

    private fun checkListing(date: String) {
        val userName = binding.etUser.editText?.text.toString()
        if(userName.isEmpty()) {
            binding.etUser.error = "Please enter user name"
        }

        val birdName = binding.etBird.editText?.text.toString()
        if(birdName.isEmpty()) {
            binding.etBird.error = "Please enter bird name"
        }

        val description = binding.etDescription.editText?.text.toString()

        if (userName.isNotEmpty() && !db.pictureUrl.isNullOrEmpty() && birdName.isNotEmpty()) {
            val listingToSave = Listing(birdName, description, db.pictureUrl!!, date, userName)
            db.saveListing(listingToSave, this)
            findNavController().navigate(R.id.action_createItem_to_homeFragment)
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == Activity.RESULT_OK && requestCode == 0) {
            pictureUri = data?.data
            binding.imageView2.setImageURI(pictureUri)
        }
    }
}
