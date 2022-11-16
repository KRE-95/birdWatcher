package com.gruppe16.birdwatcher

import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.material.navigation.NavigationView
import com.gruppe16.birdwatcher.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    // global (not null) this for navigation drawer - connect three navigation coponents.
    lateinit var drawerOpen: ImageView
    lateinit var navigationDrawer: NavigationView
    lateinit var drawerLayout: DrawerLayout
    private lateinit var viewBinding: ActivityMainBinding
    //private lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)
        // from activity_main xml - for navigation drawer
        drawerOpen=findViewById(R.id.drawer_open)
        navigationDrawer=findViewById(R.id.nav_drawer)
        drawerLayout=findViewById(R.id.drawer_layout)

        // when click
        drawerOpen.setOnClickListener {
            drawerLayout.openDrawer(GravityCompat.START)
        }
        // to connect
        val navController:NavController=Navigation.findNavController(this,R.id.fragment)
        NavigationUI.setupWithNavController(navigationDrawer,navController)

        // LocationProvider
       // fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
       // getLocation()
    }

    /*private fun getLocation() {
        val task = fusedLocationProviderClient.lastLocation

        if(ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED && ActivityCompat
                .checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
        ){
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), 101)
            return
        }
        task.addOnSuccessListener {
            if(it != null){
                println("*******************TESTING*******************")
                println("${it.latitude}, ${it.longitude}")
                println("*******************TESTING*******************")
            }
        }
    }*/

}

