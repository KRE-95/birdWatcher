package com.gruppe16.birdwatcher

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.gruppe16.birdwatcher.components.CameraX
import com.gruppe16.birdwatcher.data.Listing
import com.gruppe16.birdwatcher.data.User
import com.gruppe16.birdwatcher.databinding.FragmentHomeBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class HomeFragment : Fragment(R.layout.fragment_home) {


    private var _binding : FragmentHomeBinding? = null
    private val bindingHomeFragment get() = _binding!!
    private lateinit var cameraExecutor: ExecutorService
    private lateinit var camera: CameraX
    private  val listingCollectionRef = Firebase.firestore.collection("listings")
    private  val userCollectionRef = Firebase.firestore.collection("users")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return bindingHomeFragment.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (allPermissionsGranted()) {
            camera = CameraX(this, bindingHomeFragment)
            camera.startCamera()
        } else {
            ActivityCompat.requestPermissions(
                activity as Activity, REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS)
        }

        //Testing that firebase works
        //TODO: delete when done testing
//        val newList = mutableListOf(2)
//        val newUser = User("Anne Hansen", newList)
//        saveUser(newUser)
//
//        val newListing = Listing(
//            2,
//            "Flycatcher",
//            "https://firebasestorage.googleapis.com/v0/b/birdwatcher-3cf34.appspot.com/o/photos%2F2022-11-03-21-05-28-794?alt=media&token=10e5f211-e99d-4d1b-9648-264474c44c3b",
//            "Cool bird",
//            "Anne Hansen"
//        )
//        saveListing(newListing)

        bindingHomeFragment.captureBtnMain.setOnClickListener { camera.takePhoto(this, bindingHomeFragment) }
        cameraExecutor = Executors.newSingleThreadExecutor()
    }

    private fun uploadPhotoToFirebase(name: String, selectedPhotoUri: Uri?) {
        val storage = FirebaseStorage.getInstance().getReference("/photos/$name")

        storage.putFile(selectedPhotoUri!!)
            .addOnCanceledListener {
                Log.d(TAG, "Successfully uploaded photo.")

                storage.downloadUrl.addOnSuccessListener {
                    Log.d(TAG, "URL: $it")
                }
            }
    }

    protected fun saveUser(user: User)  = CoroutineScope(Dispatchers.IO).launch {
        try {
            userCollectionRef.add(user)
            withContext(Dispatchers.Main) {
                Toast.makeText(this@HomeFragment.requireContext(), "Saved data", Toast.LENGTH_LONG).show()
            }
        } catch(e: Exception) {
            withContext(Dispatchers.Main) {
                Toast.makeText(this@HomeFragment.requireContext(), e.message, Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun saveListing(listing: Listing)  = CoroutineScope(Dispatchers.IO).launch {
        try {
            listingCollectionRef.add(listing)
            withContext(Dispatchers.Main) {
                Toast.makeText(this@HomeFragment.requireContext(), "Saved data", Toast.LENGTH_LONG).show()
            }
        } catch(e: Exception) {
            withContext(Dispatchers.Main) {
                Toast.makeText(this@HomeFragment.requireContext(), e.message, Toast.LENGTH_LONG).show()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        cameraExecutor.shutdown()
    }

    companion object {
        private const val TAG = "birdWatcher"
        const val FILENAME_FORMAT = "yyyy-MM-dd-HH-mm-ss-SSS"
        private const val REQUEST_CODE_PERMISSIONS = 10
        private val REQUIRED_PERMISSIONS =
            mutableListOf (
                Manifest.permission.CAMERA
            ).apply {
                if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.P) {
                    add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                }
            }.toTypedArray()
    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(
            this.requireContext(), it) == PackageManager.PERMISSION_GRANTED
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String>, grantResults:
        IntArray) {
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