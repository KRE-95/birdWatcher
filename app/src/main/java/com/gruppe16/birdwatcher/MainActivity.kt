package com.gruppe16.birdwatcher

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.Toast
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import com.google.android.material.navigation.NavigationView
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.gruppe16.birdwatcher.data.User
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {
    // global (not null) this for navigation drawer - connect three navigation coponents.
    lateinit var drawerOpen: ImageView
    lateinit var navigationDrawer: NavigationView
    lateinit var drawerLayout: DrawerLayout

    private  val userCollectionRef = Firebase.firestore.collection("user")


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val newUser = User("Hi World")
        saveUser(newUser)

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

    private fun saveUser(user:User)  = CoroutineScope(Dispatchers.IO).launch {
        try {
            userCollectionRef.add(user).await()
            withContext(Dispatchers.Main) {
                Toast.makeText(this@MainActivity, "Saved data", Toast.LENGTH_LONG).show()
            }
        } catch(e: Exception) {
            withContext(Dispatchers.Main) {
                Toast.makeText(this@MainActivity, e.message, Toast.LENGTH_LONG).show()
            }
        }
    }
}