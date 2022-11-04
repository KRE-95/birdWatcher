package com.gruppe16.birdwatcher

import android.Manifest
import android.app.Activity
import android.content.ContentValues
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.gruppe16.birdwatcher.data.Listing
import com.gruppe16.birdwatcher.data.User
import com.gruppe16.birdwatcher.databinding.FragmentHomeBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.text.SimpleDateFormat
import java.util.*

class HomeFragment : Fragment(R.layout.fragment_home) {


    private var _binding : FragmentHomeBinding? = null
    private val bindingHomeFragment get() = _binding!!
    private var imageCapture: ImageCapture? = null
    private lateinit var cameraExecutor: ExecutorService
    private  val listingCollectionRef = Firebase.firestore.collection("listings")
    private  val userCollectionRef = Firebase.firestore.collection("users")

    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this.requireContext())

        cameraProviderFuture.addListener({
            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()

            val preview = Preview.Builder()
                .build()
                .also {
                    it.setSurfaceProvider(bindingHomeFragment.viewFinder.surfaceProvider)
                }

            imageCapture = ImageCapture.Builder().build()

            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

            try {
                cameraProvider.unbindAll()

                cameraProvider.bindToLifecycle(
                    this, cameraSelector, preview, imageCapture)

            } catch(exc: Exception) {
                Log.e(TAG, "Use case binding failed", exc)
            }

        }, ContextCompat.getMainExecutor(this.requireContext()))
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        if (allPermissionsGranted()) {
            startCamera()
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

        bindingHomeFragment.captureBtnMain.setOnClickListener { takePhoto() }

        cameraExecutor = Executors.newSingleThreadExecutor()
        return bindingHomeFragment.root
    }


    private fun takePhoto() {
        val imageCapture = imageCapture ?: return

        val name = SimpleDateFormat(FILENAME_FORMAT, Locale.US)
            .format(System.currentTimeMillis())
        val contentValues = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, name)
            put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg")
            if(Build.VERSION.SDK_INT > Build.VERSION_CODES.P) {
                put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/CameraX-Image")
            }
        }

        val outputOptions = context?.let {
            ImageCapture.OutputFileOptions
                .Builder( it.contentResolver,
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    contentValues)
                .build()
        }

        if (outputOptions != null) {
            Log.d(TAG,"Photo output: ${outputOptions}")
            imageCapture.takePicture(
                outputOptions,
                ContextCompat.getMainExecutor(this.requireContext()),
                object : ImageCapture.OnImageSavedCallback {
                    override fun onImageSaved(output: ImageCapture.OutputFileResults){
                        val msg = "Photo capture succeeded: ${output.savedUri}"
                        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
                        Log.d(TAG, msg)
                        bindingHomeFragment.toListingBtn.text = "Make listing"
                        bindingHomeFragment.toListingBtn.setOnClickListener { uploadPhotoToFirebase(name, output.savedUri) }
                    }
                    override fun onError(exc: ImageCaptureException) {
                        Log.e(TAG, "Photo capture failed: ${exc.message}", exc)
                    }
                }
            )
        }
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
        private const val FILENAME_FORMAT = "yyyy-MM-dd-HH-mm-ss-SSS"
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
                startCamera()
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