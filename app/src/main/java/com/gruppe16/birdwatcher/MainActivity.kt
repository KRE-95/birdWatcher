package com.gruppe16.birdwatcher

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import com.google.android.material.navigation.NavigationView

class MainActivity : AppCompatActivity() {
    // global (not null) this for navigation drawer - connect three navigation coponents.
    lateinit var drawerOpen: ImageView
    lateinit var navigationDrawer: NavigationView
    lateinit var drawerLayout: DrawerLayout


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
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


    }
}