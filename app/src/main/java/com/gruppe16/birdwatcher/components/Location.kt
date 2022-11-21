package com.gruppe16.birdwatcher.components

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.location.Geocoder
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.gruppe16.birdwatcher.databinding.FragmentCreateItemBinding
import com.gruppe16.birdwatcher.fragments.CreateItemFragment
import java.util.*

class Location(var fragment: CreateItemFragment, private var binding: FragmentCreateItemBinding) {

    private var fusedLocationProviderClient: FusedLocationProviderClient? = null
    //TODO private lateinit var geocoder: Geocoder
    //private var fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(fragment.requireContext())

    var _longitude : Double = 0.0
    val longitude : Double
        get() = _longitude

    var _latitude : Double = 0.0
    val latitude : Double
        get() = _latitude

    fun setLongitude(longitude: Double) {
        _longitude = longitude
    }

    fun setLatitude(latitude: Double) {
        _latitude = latitude
    }

    fun getLocation(fragment: Fragment) {
        //instansiere fusedLocation her eller over?
       fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(fragment.requireContext())
        val task = fusedLocationProviderClient!!.lastLocation

        if(ActivityCompat.checkSelfPermission(fragment.requireContext(), Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED && ActivityCompat
                .checkSelfPermission(fragment.requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
        ){
            ActivityCompat.requestPermissions(fragment.requireContext() as Activity, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 101)

            return
        }

        //TODO: Kommer ikke inn fordi it = null
        task.addOnSuccessListener {
            if(it != null){

                setLongitude(it.longitude)
                setLatitude(it.latitude)
                println("*******************TESTING*******************")
                println("INSIDE")
                println("${it.latitude}, ${it.longitude}")
                //println(address)
                println("*******************TESTING*******************")


                setLongitude(it.longitude)
                setLatitude(it.latitude)

                // var address = getAddress(longitude, latitude)


            }
        }
    }

    @Suppress("DEPRECATION")
    private fun getAddress(fragment: CreateItemFragment, latitude: Double, longitude: Double): String{
        var addressAsString = ""
        var geocoder = Geocoder(fragment.requireContext(), Locale.getDefault())
        var address = geocoder.getFromLocation(latitude, longitude, 1)

        if (address != null) {
            addressAsString = address[0].locality
        }
        println(addressAsString)
        return addressAsString
    }
}
