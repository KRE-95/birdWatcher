package com.gruppe16.birdwatcher.components

import android.net.Uri
import android.util.Log
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.gruppe16.birdwatcher.data.Listing
import com.gruppe16.birdwatcher.data.User
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class FirebaseDatabase {

    private  val listingCollectionRef = Firebase.firestore.collection("listings")
    private  val userCollectionRef = Firebase.firestore.collection("users")

    private var _pictureUrl : String? = null
    val pictureUrl: String?
        get() = _pictureUrl

    protected fun saveUser(user: User)  = CoroutineScope(Dispatchers.IO).launch {
        try {
            userCollectionRef.add(user)
            withContext(Dispatchers.Main) {
                //Toast.makeText(this@HomeFragment.requireContext(), "Saved data", Toast.LENGTH_LONG).show()
            }
        } catch(e: Exception) {
            withContext(Dispatchers.Main) {
                //Toast.makeText(this@HomeFragment.requireContext(), e.message, Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun saveListing(listing: Listing)  = CoroutineScope(Dispatchers.IO).launch {
        try {
            listingCollectionRef.add(listing)
            withContext(Dispatchers.Main) {
                //Toast.makeText(this@HomeFragment.requireContext(), "Saved data", Toast.LENGTH_LONG).show()
            }
        } catch(e: Exception) {
            withContext(Dispatchers.Main) {
                //Toast.makeText(this@HomeFragment.requireContext(), e.message, Toast.LENGTH_LONG).show()
            }
        }
    }

    fun uploadPhotoToFirebase(name: String, selectedPhotoUri: Uri?) {
        setPictureUrl(null)
        val storage = FirebaseStorage.getInstance().getReference("/photos/$name")

        storage.putFile(selectedPhotoUri!!)
            .addOnCompleteListener() {
                Log.d("CREATE", "Successfully uploaded photo.")

                storage.downloadUrl.addOnSuccessListener {
                    Log.d("CREATE", "URL: $it")
                    setPictureUrl(it.toString())
                    Log.d("CREATE", "PICTURE URL $pictureUrl")
                }
            }
    }

    private fun setPictureUrl(url: String?){
        _pictureUrl = url
    }
}