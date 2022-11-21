package com.gruppe16.birdwatcher.fragments

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.location.Geocoder
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.constraintlayout.motion.widget.Debug.getLocation
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.gruppe16.birdwatcher.R
import com.gruppe16.birdwatcher.components.CameraX
import com.gruppe16.birdwatcher.databinding.FragmentHomeBinding
import com.gruppe16.birdwatcher.viewmodels.SharedViewModel
import java.util.*
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class HomeFragment : Fragment(R.layout.fragment_home) {

    private val viewModel: SharedViewModel by activityViewModels()
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var cameraExecutor: ExecutorService
    private lateinit var camera: CameraX
    //  private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    // private lateinit var geocoder: Geocoder

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (allPermissionsGranted()) {
            camera = CameraX(this, binding)
            camera.startCamera()
            // LocationProvider
            // fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(activity as Context)
            //   getLocation()


        } else {
            ActivityCompat.requestPermissions(
                activity as Activity, REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS
            )
        }

        binding.captureBtnMain.setOnClickListener {
            camera.takePhoto(this, binding)
            viewModel.setPictureUri(camera.pictureUri)
            viewModel.setDate(camera.date)
//            println("*******************TESTING*******************")
//            println("OUTSIDE")
//            println(getLocation())
//            println("*******************TESTING*******************")
            //TODO: use longitude and latitude here to define the place and send that info to createdItem
        }
        cameraExecutor = Executors.newSingleThreadExecutor()
    }

    override fun onDestroy() {
        super.onDestroy()
        cameraExecutor.shutdown()
    }

    companion object {
        const val REQUEST_CODE_PERMISSIONS = 10
        val REQUIRED_PERMISSIONS =
            mutableListOf(
                //TODO: COULD ALL CHECKS BE DONE HERE instead of an own check for location in getLocation ?
                Manifest.permission.CAMERA//, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION
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

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String>, grantResults:
        IntArray
    ) {
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (allPermissionsGranted()) {
                camera.startCamera()

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

/*
  private fun getLocation() {
        val task = fusedLocationProviderClient.lastLocation

        if(ActivityCompat.checkSelfPermission(activity as Context, Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED && ActivityCompat
                .checkSelfPermission(activity as Context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
        ){
            ActivityCompat.requestPermissions(context as Activity, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 101)
            return
        }
        task.addOnSuccessListener {
            if(it != null){

                var longitude = it.longitude
                var latitude = it.latitude
                var address = getAddress(longitude, latitude)


                //TODO: REMOVE TEST PRINTING
                println("*******************TESTING*******************")
                println("INSIDE")
                println("${it.latitude}, ${it.longitude}")
                println(address)
                println("*******************TESTING*******************")
            }
        }
    }

    @Suppress("DEPRECATION")
    private fun getAddress(latitude: Double, longitude: Double): String{
        var addressAsString = ""
        var geocoder = Geocoder(activity as Context, Locale.getDefault())
        var address = geocoder.getFromLocation(latitude, longitude, 1)

        if (address != null) {
            addressAsString = address[0].locality
        }
        println(addressAsString)
        return addressAsString
    }
}*/
