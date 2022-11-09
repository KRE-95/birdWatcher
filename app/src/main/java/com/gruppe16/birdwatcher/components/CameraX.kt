package com.gruppe16.birdwatcher.components

import android.content.ContentValues
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import com.gruppe16.birdwatcher.HomeFragment
import com.gruppe16.birdwatcher.R
import com.gruppe16.birdwatcher.databinding.FragmentHomeBinding
import java.text.SimpleDateFormat
import java.util.*

class CameraX(var fragment: HomeFragment, var binding: FragmentHomeBinding) {
    private var imageCapture: ImageCapture? = null
    private var TAG = "HomeFragment"

    fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(fragment.requireContext())

        cameraProviderFuture.addListener({
            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()

            val preview = Preview.Builder()
                .build()
                .also {
                    it.setSurfaceProvider(binding.viewFinder.surfaceProvider)
                }

            imageCapture = ImageCapture.Builder().build()

            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

            try {
                cameraProvider.unbindAll()

                cameraProvider.bindToLifecycle(
                    fragment, cameraSelector, preview, imageCapture)

            } catch(exc: Exception) {
                Log.e(TAG, "Use case binding failed", exc)
            }

        }, ContextCompat.getMainExecutor(fragment.requireContext()))
    }

    fun takePhoto(fragment: HomeFragment, binding: FragmentHomeBinding) {
        val imageCapture = imageCapture ?: return

        val name = SimpleDateFormat(HomeFragment.FILENAME_FORMAT, Locale.US)
            .format(System.currentTimeMillis())
        val contentValues = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, name)
            put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg")
            if(Build.VERSION.SDK_INT > Build.VERSION_CODES.P) {
                put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/CameraX-Image")
            }
        }

        val outputOptions = fragment.context?.let {
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
                ContextCompat.getMainExecutor(fragment.requireContext()),
                object : ImageCapture.OnImageSavedCallback {
                    override fun onImageSaved(output: ImageCapture.OutputFileResults){
                        val msg = "Photo capture succeeded: ${output.savedUri}"
                        Toast.makeText(fragment.context, msg, Toast.LENGTH_SHORT).show()
                        Log.d(TAG, msg)
                        binding.toListingBtn.text = "Make listing"
                        binding.toListingBtn.setOnClickListener {
                            //.uploadPhotoToFirebase(name, output.savedUri)
                            fragment.findNavController().navigate(R.id.action_homeFragment_to_createItem)}
                    }
                    override fun onError(exc: ImageCaptureException) {
                        Log.e(TAG, "Photo capture failed: ${exc.message}", exc)
                    }
                }
            )
        }
    }
}