@file:Suppress("DEPRECATION")

package com.gruppe16.birdwatcher.fragments

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.gruppe16.birdwatcher.R
import com.gruppe16.birdwatcher.components.FirebaseDatabase
import com.gruppe16.birdwatcher.components.Location
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
    private lateinit var location: Location
    private var longitude: Double = 0.0
    private var latitude: Double = 0.0



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

        //TODO instansierer location
        location = Location(this, binding)

        //TODO hvis vi har tilgangene så kjøres logikken
        if (allPermissionsGranted()) {
            location.getLocation(this)
            //TODO await?

            println(location.latitude)
            //  println(location.latitude)

        } else {
            ActivityCompat.requestPermissions(
                activity as Activity,
                CreateItemFragment.REQUIRED_PERMISSIONS,
                REQUEST_CODE_PERMISSIONS
            )
        }

        pictureUri = viewModel.pictureUri.value?.toUri()
        val date = viewModel.date.value.toString()
        val pictureId = UUID.randomUUID().toString()
        binding.imageView2.setImageURI(pictureUri)
        binding.editTextDate.text = date

        if (viewModel.pictureUri.value.isNullOrEmpty()) {
            noPictureToast()
        }

        binding.keepPictureBtn.setOnClickListener {
            keepPicture(pictureId)
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

    companion object {
         const val REQUEST_CODE_PERMISSIONS = 10
        private val REQUIRED_PERMISSIONS =
            mutableListOf(
                //TODO: COULD ALL CHECKS BE DONE HERE instead of an own check for location in getLocation ?
                Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION
            ).apply {
                if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.P) {
                    add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                }
            }.toTypedArray()
    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(
            this.requireContext(), it
        ) == PackageManager.PERMISSION_GRANTED
    }

    //TODO: hvorfor får ikke jeg beskjed om deprecation her, men den er markert deprecated i HomeFragment
    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String>, grantResults:
        IntArray
    ) {
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (allPermissionsGranted()) {
                location.getLocation(this)

            } else {
                Toast.makeText(
                    context,
                    "Permissions not granted by the user.",
                    Toast.LENGTH_SHORT
                ).show()
            }
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

    private fun keepPicture(pictureId: String) {
        if(pictureUri.toString().isNotEmpty()) {
            db.uploadPhotoToStorage(pictureId, pictureUri)
            binding.saveListing.isEnabled = true
        } else {
            noPictureToast()
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

    private fun noPictureToast() {
        val toast = Toast(this@CreateItemFragment.requireContext())

        toast.apply {
            val layout = layoutInflater.inflate(R.layout.error_toast, null)
            layout.findViewById<TextView>(R.id.tVToast).text = "No picture? Use Change Picture"
            setGravity(Gravity.CENTER, 0, 0)
            duration = Toast.LENGTH_LONG
            view = layout
            show()
        }
    }
}
