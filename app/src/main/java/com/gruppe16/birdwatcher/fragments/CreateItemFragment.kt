package com.gruppe16.birdwatcher.fragments

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
    private lateinit var db : FirebaseDatabase

    // TODO: må ha en instans av location
    private lateinit var location: Location

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
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
                HomeFragment.REQUIRED_PERMISSIONS,
                HomeFragment.REQUEST_CODE_PERMISSIONS
            )
        }

        val pictureUri = viewModel.pictureUri.value?.toUri()
        val date = viewModel.date.value.toString()
        val pictureId = UUID.randomUUID().toString()
        binding.imageView2.setImageURI(pictureUri)
        binding.editTextDate.setText(date)

        //TODO binding for get location button?

        binding.button.setOnClickListener {
            db.uploadPhotoToStorage(pictureId, pictureUri)
            binding.saveListing.isEnabled = true
        }

        binding.cancelListing.setOnClickListener {
            if (db.pictureUrl == null) {
                db.deletePhotoFromStorage(pictureId)
            }
            findNavController().navigate(R.id.action_createItem_to_homeFragment)
        }

        binding.saveListing.setOnClickListener {
            val userName = binding.etUser.editText?.text.toString()
            val description = binding.etDescription.editText?.text.toString()
            val listingToSave = Listing("TODO", description, db.pictureUrl!!, date, userName)
            db.saveListing(listingToSave, this)
            Log.d("CREATE", "LISTING: ${listingToSave.picture} og ${listingToSave.description}")
            findNavController().navigate(R.id.action_createItem_to_homeFragment)
        }

    }

    companion object {
        private const val REQUEST_CODE_PERMISSIONS = 10
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

    private fun allPermissionsGranted() = HomeFragment.REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(
            this.requireContext(), it
        ) == PackageManager.PERMISSION_GRANTED
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String>, grantResults:
        IntArray
    ) {
        if (requestCode == HomeFragment.REQUEST_CODE_PERMISSIONS) {
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
}
